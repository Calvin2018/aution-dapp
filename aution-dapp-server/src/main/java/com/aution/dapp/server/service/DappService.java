package com.aution.dapp.server.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	
	public JSONObject getBalance(String userId,String amount,String feeAmount) throws IOException {
		
		if(Strings.isNullOrEmpty(userId))throw new IllegalArgumentException("Arguments userId are required");
		
		JSONObject obj = new  JSONObject();
		obj.put("flag", true);
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		String accessToken = appClient.getAccessToken();
		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		
		RestApiResponse<Map<String,String>> temp = dBaseApiService.doQuery(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, userId, amount, feeAmount, typeToken,appClient);
		if(!Strings.isNullOrEmpty(amount)) {
			if(!temp.getCode().equals(ApiConstants.CODE_SUCCESS)) 
				obj.put("flag", false);
				
		}else {
			String balance =  temp.getData().get("balance");
			obj.put("balance", balance);
		}
		return obj;
	}
	
	
	public Map<String,String> getUserInfo(String authToken) throws IOException {
		
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		Map<String,String> map = dBaseApiService.getUserInfo(authToken, typeToken).getData();
		return map;
	}
	
	public Map<String,String> getUserInfoUserId(String userId) throws IOException {
		if(Strings.isNullOrEmpty(userId))throw new IllegalArgumentException("Arguments userId are required");
		
		Goods goods = userRepository.findUserByUserId(userId);
		Map<String,String> map = new HashMap<String,String>();
		if(null == goods||Strings.isNullOrEmpty(goods.getUserName())) throw new IllegalArgumentException("userId is not exist!");
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
		
		if(Strings.isNullOrEmpty(gId)||Strings.isNullOrEmpty(userId)||null == price)throw new IllegalArgumentException("Arguments gId userId and price are required");
		
		
		JSONObject obj = new JSONObject();
		//检查竞拍价格比当前价格高
		History temp = hRepository.findHistoryByUserIdAndGoodsId(userId, gId);
		Goods goods = temp.getGoods();
		
		Long currentTime = System.currentTimeMillis();
		if(currentTime > goods.getEndTime()) {
			obj.put("flag", false);
			obj.put("msg", "拍卖已结束");
			return  obj;
		}
		
		Double maxPrice = null;
		if(null==goods.getCurrentPrice()) {
			maxPrice = goods.getStartPrice();
			if(price < maxPrice) throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),"Current price is higher than  bid price");
		}else {
			maxPrice = goods.getCurrentPrice();
			if(price <= maxPrice) throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),"Current price is higher than  bid price");
		}
		Double bidPrice = price;
		
		price = price - temp.getBidPrice();
		
		obj = getBalance(userId,String.valueOf(price),null);
		boolean flag = (boolean)obj.get("flag");
		 
		if(!flag) return obj;
			
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
		history.setBidTime(new Date().getTime());
		history.setPayPrice(price);
		//判断此次竞拍是否支付 0：表示未支付 1：表示支付成功
		history.setTemp("0");
		Integer hFlag = hRepository.insertHistory(history);
		//用于数据库回滚
		if(0 == hFlag) throw new ApiException("History Insert Failed");
		
			
		obj.put("flag", true);
		obj.put("pay_url", payUrl);
		return obj;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public String doPaySuccessed(History history,Double price,PayNotifyBean notifyBean) throws ApiException, ParseException {
		
		LOGGER.debug("start update table t_history,tradeNo: {}",notifyBean.getTradeNo());
        hRepository.updateHistory("1",notifyBean.getTradeNo());
		LOGGER.debug("finnish update table t_history,tradeNo: {}",notifyBean.getTradeNo());
		
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		long date = format.parse(notifyBean.getPayTime()).getTime();
		 
		//业务查询 
		String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
		
		Transaction transaction = new Transaction();
		transaction.setGoodsId(history.getGoodsId());
		transaction.setFromUserId(history.getUserId());
		transaction.setPrice(price);
		transaction.setToUserId(transferId);
		transaction.setTxId(notifyBean.getCoinTradeNo());
		transaction.setTxTime(date);
		tRepository.insertTransaction(transaction);
		//用于数据库回滚
		
		if(date > history.getGoods().getEndTime()) {
	        	
        	TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
            String accessToken = appClient.getAccessToken();
            DBaseApiService dBaseApiService = appClient.getdBaseApiService();
            BusinessRecord businessRecord= new BusinessRecord();
            businessRecord.setUserId(history.getUserId());
    		businessRecord.setAmount(new BigDecimal(price));
    		businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
    		Map<String, String> data = null;
        	try {
        		data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
			
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
	        		 hRepository.updateHistory("2",notifyBean.getCoinTradeNo());
	        	}
	        	return "FAILED";
	        }catch (IOException e) {
				LOGGER.error(e.getMessage());
				return "FAILED";
			}
			
        }
        		
		Double maxPrice = hRepository.findMaxPriceByGid(history.getGoodsId());
		
		if(history.getBidPrice() >= maxPrice) {
			Goods goods = new Goods();
			goods.setGoodsId(history.getGoodsId());
			goods.setCurrentPrice(history.getBidPrice());
		
			goodsRepository.updateGoods(goods);
		} 
		
		return "SUCCESS";
		
	}
	
	
	public void bidCompleted(String gId,String sellerId) throws IOException{
		
		List<History> historyList = hRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(0, Integer.MAX_VALUE));
		if(null != historyList&&historyList.size()>0)
			bidCompletedMethod(historyList,gId,sellerId,historyList.get(0).getGoods().getCurrentPrice());
	}
	public void bidCompletedMethod(List<History> historyList,String gId,String sellerId,Double currentPrice) throws IOException {
		//没人竞拍
		Goods goods = new Goods();
		goods.setGoodsId(gId);
		
		DBaseApiService dBaseApiService = appClient.getdBaseApiService();
		BusinessRecord businessRecord= new BusinessRecord();

		TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
		String accessToken = appClient.getAccessToken();
		boolean flag = true;
		for(int i=0;i<historyList.size();i++) {
			
			History history = historyList.get(i);
			businessRecord.setUserId(history.getUserId());
			businessRecord.setAmount(new BigDecimal(history.getBidPrice()));
			businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
			Map<String, String> data = null;
			
			if(history.getBidPrice().equals(currentPrice)) {
				if(true == flag && history.getGoods().getBuyerId().equals("0")) {
					//拍卖成功
					msgRepository.insertMessage(sellerId, gId, '1', '0');
					//竞拍成功
					msgRepository.insertMessage(history.getUserId(), gId, '3', '0');
					
					goods.setBuyerId(history.getUserId());
					businessRecord.setUserId(sellerId);
					data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
					flag = false;
					
				}
			}else {
				//竞拍失败
				msgRepository.insertMessage(history.getUserId(), gId, '4', '0');
				data = dBaseApiService.doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken, businessRecord, typeToken,appClient).getData();
			}
			
		
			if(null == data) throw new ApiException("Transaction failed");
			long time = System.currentTimeMillis();
			Transaction transaction = new Transaction();
			transaction.setGoodsId(gId);
			transaction.setFromUserId(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID));
			transaction.setPrice(history.getBidPrice());
			transaction.setToUserId(businessRecord.getUserId());
			transaction.setTxId(data.get(ApiConstants.X_TRADE_NO));
			transaction.setTxTime(time);
			transaction.setTemp("");
			Integer tFlag = tRepository.insertTransaction(transaction);
			//用于数据库回滚
			if(0 == tFlag) throw new ApiException("Transaction Insert Failed");
		}
		
		if(historyList.size()<=0) {
			goods.setStatus(3);
			goodsRepository.updateGoods(goods);
			msgRepository.insertMessage(sellerId, gId, '2', '0');
		}else {
			goods.setStatus(2);
			goodsRepository.updateGoods(goods);
		}
	}
	public List<List<History>> findHistoryForNoIssueOrder(){
		//多加1分钟 是为了避免两个定时任务 同时执行
		Long endTime = System.currentTimeMillis()+60000l;
		
		List<History> list = hRepository.findTransactionForNoIssueOrder(endTime);
		List<List<History>> result = null;
		if(null != list && list.size() >0) {
			result = new ArrayList<List<History>>();
			Set<String> set = new HashSet<String>();
			List<History> hList = null;
			for(int i=0;i<list.size();i++) {
				History temp = list.get(i);
				if( null != temp.getTemp()) {
					continue;
				}
				if(!set.contains(temp.getGoodsId())) {
					set.add(temp.getGoodsId());
					if(null != hList&&hList.size()>0) result.add(hList);
					hList = new ArrayList<History>();
				}
				
				hList.add(temp);
				
				if(i == list.size() -1) result.add(hList);
				
			}
			return result;
		}else {
			return null;
		}
	}

	public void findHistoryOfIssueFailed() throws IOException{
		String temp = "2";
		List<History> list =  hRepository.findHistoryByTemp(temp);
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
