package com.aution.dapp.server.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.core.RestApiResponse;
import com.aution.dapp.server.core.internal.DBaseApiService;
import com.aution.dapp.server.model.BusinessRecord;
import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.model.PayNotifyBean;
import com.aution.dapp.server.model.PayRequest;
import com.aution.dapp.server.model.Transaction;
import com.aution.dapp.server.repository.GoodsRepository;
import com.aution.dapp.server.repository.HistoryRepository;
import com.aution.dapp.server.repository.MessageRepository;
import com.aution.dapp.server.repository.TransactionRepository;
import com.aution.dapp.server.repository.UserInfoRepository;
import com.aution.dapp.server.utils.GenerateNoUtil;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;



/**
 * 拍卖服务类
 * @author Administrator
 *
 */
@Service
@Transactional
public class DappService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DappService.class);
	
	@Autowired
	private HistoryRepository hRepository;
	
	@Autowired
	private TransactionRepository tRepository;
	
	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private MessageRepository msgRepository;
	
	@Autowired
	private UserInfoRepository userRepository;
	
	
	private static AppClient appClient = AppClient.getInstance();

	/**
	 * 获取用户余额 不提供
	 * @param userId
	 * @param amount
	 * @param feeAmount
	 * @return
	 * @throws IOException
	 */
	public JSONObject getBalance(String userId,String amount,String feeAmount) throws IOException {
		
		if(Strings.isNullOrEmpty(userId)) {
            throw new IllegalArgumentException("Arguments userId are required");
        }

		JSONObject obj = new  JSONObject();
		obj.put("flag", true);
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		String accessToken = appClient.getAccessToken();
		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		
		RestApiResponse<Map<String,String>> temp = dBaseApiService.doQuery(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, userId, amount, feeAmount, typeToken,appClient);
		if(!Strings.isNullOrEmpty(amount)) {
			if(!temp.getCode().equals(ApiConstants.CODE_SUCCESS)) {
				obj.put("flag", false);
			}
				
		}else {
			String balance =  temp.getData().get("balance");
			obj.put("balance", balance);
		}
		return obj;
	}

	/**
	 * 调用灵光币接口获取用户信息
	 * @param authToken
	 * @return
	 * @throws IOException
	 */
	public Map<String,String> getUserInfo(String authToken) throws IOException {
		
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		Map<String,String> map = dBaseApiService.getUserInfo(authToken, typeToken).getData();
		return map;
	}

	/**
	 * 查询本地数据库获取用户信息
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public Map<String,String> getUserInfoUserId(String userId) throws IOException {
		if(Strings.isNullOrEmpty(userId)) {
			throw new IllegalArgumentException("Arguments userId are required");
		}
		
		Goods goods = userRepository.findUserByUserId(userId);
		Map<String,String> map = new HashMap<String,String>();
		if(null == goods||Strings.isNullOrEmpty(goods.getUserName())) {
            throw new IllegalArgumentException("userId is not exist!");
        }
		//TODO 带修改
		map.put("job_number", userId);
		map.put("avatar", goods.getAvatar());
		map.put("user_name",goods.getUserName());
		map.put("user_phone", goods.getUserPhone());
		return map;
	}
	/**
	 * 调用灵光币接口创建交易 
	 * @param gId 商品id
	 * @param userId 竞拍者用户id
	 * @param price 竞拍价格
	 * @return
	 * @throws Exception 
	 * 注： 支付完成需要通过灵光币通知，这里判断不了是否用户已经支付成功
	 *    通知url需要提供
	 */
	
	public JSONObject createOrder(String gId,String userId,Double price) throws Exception {

		JSONObject obj = new JSONObject();
		//检查竞拍价格比当前价格高
		History temp = hRepository.findHistoryByUserIdAndGoodsId(userId, gId);
		
		
		Long currentTime = System.currentTimeMillis();
		if(currentTime > temp.getEndTime()) {
			obj.put("flag", false);
			obj.put("msg", "拍卖已结束");
			return  obj;
		}
		
		Double maxPrice = null;
		if(null==temp.getCurrentPrice()) {
			maxPrice = temp.getStartPrice();
			if(price < maxPrice) {
				throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),"Current price is higher than  bid price");
			}
		}else {
			maxPrice = temp.getCurrentPrice();
			if(price <= maxPrice) {
				throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),"Current price is higher than  bid price");
			}
		}
		Double bidPrice = price;
		
		if(null != temp.getBidPrice()) {
			price = price - temp.getBidPrice();
		}
		
		obj = getBalance(userId,String.valueOf(price),null);
		boolean flag = (boolean)obj.get("flag");
		 
		if(!flag) {
			return obj;
		}
			
		price = new BigDecimal(price).setScale(2,   BigDecimal.ROUND_HALF_DOWN).doubleValue();
		Properties configuration = appClient.getConfiguration();
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		
		String accessToken = appClient.getAccessToken();
		String tradeNo = GenerateNoUtil.generateTradeNo();
		PayRequest payRequest = new PayRequest();
		payRequest.setAmount(new BigDecimal(price));
		// 支付结果提醒url 必填
		payRequest.setNotifyUrl(configuration.getProperty(ApiConstants.DA_NOTIFY_URL));
		payRequest.setUserId(userId);
		payRequest.setDetail("");
		payRequest.setOrderDetailUrl(configuration.getProperty(ApiConstants.DA_DETAIL_URL));
		payRequest.setTitle("竞拍");
		payRequest.setTradeNo(tradeNo);
		
		String payUrl = dBaseApiService.doOrder(configuration.getProperty(ApiConstants.DA_APPID), accessToken, payRequest,appClient);
		
		History history = new History();
		history.setTradeNo(tradeNo);
		history.setGoodsId(gId);
		history.setUserId(userId);
		history.setBidPrice(bidPrice);
		history.setBidTime(System.currentTimeMillis());
		history.setPayPrice(price);
		//判断此次竞拍是否支付 0：表示未支付 1：表示支付成功
		history.setTemp("0");
		Integer hFlag = hRepository.insertHistory(history);
		//用于数据库回滚
		if(0 == hFlag) {
			throw new ApiException("History Insert Failed");
		}
		
			
		obj.put("flag", true);
		obj.put("pay_url", payUrl);
		return obj;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public String doPaySuccessed(History history,Double price,PayNotifyBean notifyBean) throws ApiException, ParseException {

		//进入该方法表示用户已经支付成功，因此更新交易状态为1即已经支付
		LOGGER.debug("start update table t_history,tradeNo: {}",notifyBean.getTradeNo());
        hRepository.updateHistory("1",notifyBean.getTradeNo());
		LOGGER.debug("finnish update table t_history,tradeNo: {}",notifyBean.getTradeNo());
		
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		long date = format.parse(notifyBean.getPayTime()).getTime();
		 
		//业务查询 
		String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
		//插入交易到本地数据库
		Transaction transaction = new Transaction();
		transaction.setGoodsId(history.getGoodsId());
		transaction.setFromUserId(history.getUserId());
		transaction.setPrice(price);
		transaction.setToUserId(transferId);
		transaction.setTxId(notifyBean.getBusinessNo());
		transaction.setTxTime(date);
		tRepository.insertTransaction(transaction);

		//当支付时间超过商品竞拍截止时间则直接发起退款
		if(date > history.getEndTime()) {
	        	
            String accessToken = appClient.getAccessToken();
            DBaseApiService dBaseApiService = appClient.getdBaseApiService();
            BusinessRecord businessRecord= new BusinessRecord();
            businessRecord.setUserId(history.getUserId());
    		businessRecord.setAmount(new BigDecimal(price));
    		businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
    		Map<String, String> data = null;
        	try {
        		data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, new TypeToken<RestApiResponse<Map<String,String>>>(){},appClient).getData();
			
				transaction.setGoodsId(history.getGoodsId());
				transaction.setFromUserId(transferId);
				transaction.setPrice(price);
				transaction.setToUserId( history.getUserId());
				transaction.setTxId(data.get(ApiConstants.X_TRADE_NO));
				transaction.setTxTime(System.currentTimeMillis());
				tRepository.insertTransaction(transaction);
				
				return "SUCCESS";
				
        	} catch(ApiException e) {
	        	if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_INSUFFICIENT_BALANCE)) {
	        		 //退款失败则修改交易状态为2即退款失败
	        		 hRepository.updateHistory("2",notifyBean.getBusinessNo());
	        	}
	        	return "FAILED";
	        }catch (IOException e) {
				LOGGER.error(e.getMessage());
				return "FAILED";
			}
			
        }
        		
		Double maxPrice = hRepository.findMaxPriceByGid(history.getGoodsId());
		//当用户1、用户2同时进入支付界面时此时是在灵光币平台本系统无法得知那个支付的金额大因此需要进行验证
		//验证 当支付金额是最大值时更新当前商品竞拍价格
		if(history.getBidPrice() >= maxPrice) {
			Goods goods = new Goods();
			goods.setGoodsId(history.getGoodsId());
			goods.setCurrentPrice(history.getBidPrice());
		
			goodsRepository.updateGoods(goods);
		} 
		
		return "SUCCESS";
		
	}

	/**
	 * 竞拍完成时定时任务调用
	 * @param gId
	 * @param sellerId
	 * @throws IOException
	 */
	public void bidCompleted(String gId,String sellerId) throws IOException{
		
		List<History> historyList = hRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(0, Integer.MAX_VALUE));
		if(null != historyList&&historyList.size()>0) {
			bidCompletedMethod(historyList,gId,sellerId,historyList.get(0).getCurrentPrice());
		}
	}
	public void bidCompletedMethod(List<History> historyList,String gId,String sellerId,Double currentPrice) throws IOException {

		Goods goods = new Goods();
		goods.setGoodsId(gId);

		//没人竞拍
		if(historyList.size()<=0) {
			goods.setStatus(3);
			goodsRepository.updateGoods(goods);
			msgRepository.insertMessage(sellerId, gId, '2', '0');
			return;
		}
		
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		BusinessRecord businessRecord= new BusinessRecord();

		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		String accessToken = appClient.getAccessToken();
		//判断是否是买家
		boolean flag = true;
		for(int i=0;i<historyList.size();i++) {
			
			History history = historyList.get(i);
			businessRecord.setUserId(history.getUserId());
			businessRecord.setAmount(new BigDecimal(history.getBidPrice()));
			businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
			Map<String, String> data = null;

			if(true == flag&&history.getBidPrice().equals(currentPrice)) {
				//拍卖成功
				msgRepository.insertMessage(sellerId, gId, '1', '0');
				//竞拍成功
				msgRepository.insertMessage(history.getUserId(), gId, '3', '0');

				goods.setBuyerId(history.getUserId());
				businessRecord.setUserId(sellerId);
				//给卖家付款
				data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
				goods.setStatus(2);
				goodsRepository.updateGoods(goods);
				flag = false;

			}else {
				//竞拍失败
				msgRepository.insertMessage(history.getUserId(), gId, '4', '0');
				data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
			}

		
			if(null == data) {
				throw new ApiException("Transaction failed");
			}
			//插入交易数据到本地数据库
			long time = System.currentTimeMillis();
			Transaction transaction = new Transaction();
			transaction.setGoodsId(gId);
			transaction.setFromUserId(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID));
			transaction.setPrice(history.getBidPrice());
			transaction.setToUserId(businessRecord.getUserId());
			transaction.setTxId(data.get(ApiConstants.X_TRADE_NO));
			transaction.setTxTime(time);
			transaction.setTemp("");
			tRepository.insertTransaction(transaction);

		}

	}

	/**
	 * 查询未退款交易并根据商品id进行分类 一个商品一个list
	 * @return
	 */
	public List<List<History>> findHistoryForNoIssueOrder(){
		//多加1分钟 是为了避免两个定时任务 同时执行
		Long endTime = System.currentTimeMillis()+60000L;
		//没有数据返回的结果为list,第一个元素为Null
		List<History> list = hRepository.findTransactionForNoIssueOrder(endTime);
		if(null != list) {
            list.removeAll(Collections.singleton(null));
        }
		List<List<History>> result = null;
		if(null != list && !list.isEmpty()) {
			result = new ArrayList<>();
			Set<String> set = new HashSet<String>();
			List<History> hList = null;
			for(int i=0;i<list.size();i++) {
				History temp = list.get(i);
				LOGGER.info("History:{}",temp);
				if( null != temp.getTemp()) {
					continue;
				}
				if(!set.contains(temp.getGoodsId())) {
					set.add(temp.getGoodsId());
					if(null != hList&&hList.size()>0) {
						result.add(hList);
					}
					hList = new ArrayList<>();
				}
				
				hList.add(temp);
				
				if(i == list.size() -1) {
					result.add(hList);
				}
				
			}
			return result;
		}else {
			return null;
		}
	}

	/**
	 * 查询定时任务退款失败的交易
	 * @throws IOException
	 */
	public void findHistoryOfIssueFailed() throws IOException{
		String temp = "2";
		List<History> list =  hRepository.findHistoryByTemp(temp);
		list.removeAll(Collections.singleton(null));
		for(History history:list) {
			
			TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
            String accessToken = appClient.getAccessToken();
            DBaseApiService dBaseApiService = appClient.getdBaseApiService();
            BusinessRecord businessRecord= new BusinessRecord();
            businessRecord.setUserId(history.getUserId());
    		businessRecord.setAmount(new BigDecimal(history.getPayPrice()));
    		businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
        
    		Map<String,String>	data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
			
			String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
			Transaction transaction = new Transaction();
			transaction.setGoodsId(history.getGoodsId());
			transaction.setFromUserId(transferId);
			transaction.setPrice(history.getPayPrice());
			transaction.setToUserId( history.getUserId());
			transaction.setTxId(data.get(ApiConstants.X_TRADE_NO));
			transaction.setTxTime(System.currentTimeMillis());
			tRepository.insertTransaction(transaction);
			hRepository.updateHistory("3", data.get(ApiConstants.X_TRADE_NO));
			
		}
		
	}
}
