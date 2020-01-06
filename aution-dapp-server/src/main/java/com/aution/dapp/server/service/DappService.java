package com.aution.dapp.server.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

import com.aution.dapp.server.quartz.BidJob;
import com.mchange.v2.holders.SynchronizedCharHolder;
import org.quartz.*;
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
 *
 * @author Administrator
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
     */
    public JSONObject getBalance(String userId, String amount, String feeAmount)
            throws IOException {

        if (Strings.isNullOrEmpty(userId)) {
            throw new IllegalArgumentException("Arguments userId are required");
        }

        JSONObject obj = new JSONObject();
        obj.put("flag", true);
        DBaseApiService dBaseApiService = appClient.getdBaseApiService();
        String accessToken = appClient.getAccessToken();
        TypeToken<RestApiResponse<Map<String, String>>> typeToken = new TypeToken<RestApiResponse<Map<String, String>>>() {
        };

        RestApiResponse<Map<String, String>> temp = dBaseApiService
                .doQuery(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID),
                        accessToken, userId, amount, feeAmount, typeToken, appClient);
        if (!Strings.isNullOrEmpty(amount)) {
            if (!temp.getCode().equals(ApiConstants.CODE_SUCCESS)) {
                obj.put("flag", false);
            }

        } else {
            String balance = temp.getData().get("balance");
            obj.put("balance", balance);
        }
        return obj;
    }

    /**
     * 查询交易是否存在
     */
    public Map<String, Object> doQueryTxStatus(String tradeNo) {

        if (Strings.isNullOrEmpty(tradeNo)) {
            throw new IllegalArgumentException("Arguments tradeNo are required");
        }
        //0，1是对接接口返回的；2，3是自定义的；0：交易完成，1：交易中，2：交易不存在，3：接口异常
        String status = "3";
        Map<String, Object> temp = null;
        try {
            DBaseApiService dBaseApiService = appClient.getdBaseApiService();
            String accessToken = appClient.getAccessToken();
            TypeToken<RestApiResponse<Map<String, Object>>> typeToken = new TypeToken<RestApiResponse<Map<String, Object>>>() {
            };

            temp = dBaseApiService.doQueryTxStatus(
                    appClient.getConfiguration().getProperty(ApiConstants.DA_APPID), accessToken,
                    tradeNo, typeToken, appClient).getData();

            if (null == temp) {
                temp = new HashMap<>();
                status = "2";
                temp.put("status",status);
            }
        } catch (ApiException e) {
            //交易不存在
            if (String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_TRANSACTION_NOT_EXIST)) {
                status = "2";
                if(null == temp){
                    temp = new HashMap<>();
                    temp.put("status",status);
                }
            }
        } catch (Exception e) {
            status = "3";
            if(null == temp) {
                temp = new HashMap<>();
                temp.put("status", status);
            }
        }

        return temp;
    }

    /**
     * 调用灵光币接口获取用户信息
     */
    public Map<String, String> getUserInfo(String authToken) throws IOException {

        DBaseApiService dBaseApiService = appClient.getdBaseApiService();
        TypeToken<RestApiResponse<Map<String, String>>> typeToken = new TypeToken<RestApiResponse<Map<String, String>>>() {
        };
        String accessToken = appClient.getAccessToken();
        Properties configuration = appClient.getConfiguration();
        String appid = configuration.getProperty(ApiConstants.DA_APPID);
        Map<String, String> map = dBaseApiService.getUserInfo(authToken, accessToken, appid, typeToken,appClient).getData();
        return map;
    }

    /**
     * 查询本地数据库获取用户信息
     */
    public Map<String, String> getUserInfoUserId(String userId) throws IOException {
        if (Strings.isNullOrEmpty(userId)) {
            throw new IllegalArgumentException("Arguments userId are required");
        }

        Goods goods = userRepository.findUserByUserId(userId);
        Map<String, String> map = new HashMap<String, String>();
        if (null == goods || Strings.isNullOrEmpty(goods.getUserName())) {
            throw new IllegalArgumentException("userId is not exist!");
        }
        //TODO 带修改 job_number 字段名称
        map.put("job_number", userId);
        map.put("avatar", goods.getAvatar());
        map.put("user_name", goods.getUserName());
        map.put("user_phone", goods.getUserPhone());
        return map;
    }

    /**
     * 调用灵光币接口创建交易
     *
     * @param gId 商品id
     * @param userId 竞拍者用户id
     * @param price 竞拍价格
     * @throws Exception 注： 支付完成需要通过灵光币通知，这里判断不了是否用户已经支付成功 通知url需要提供
     */

    public JSONObject createOrder(String gId, String userId, Double price) throws Exception {

        JSONObject obj = new JSONObject();
        //检查竞拍价格比当前价格高
        History temp = hRepository.findMaxHistoryByUserIdAndGoodsId(userId, gId);

        if(null == temp){
            throw new ApiException(Integer.parseInt(ApiConstants.CODE_GOODS_NOT_EXIST),
                    "This goods is not exist!");
        }

        Long currentTime = System.currentTimeMillis();
        if (currentTime > temp.getEndTime()) {
            obj.put("flag", false);
            obj.put("msg", "拍卖已结束");
            return obj;
        }

        Double maxPrice = null;
        if (null == temp.getCurrentPrice()) {
            maxPrice = temp.getStartPrice();
            if (price < maxPrice) {
                throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),
                        "Current price is higher than  bid price");
            }
        } else {
            maxPrice = temp.getCurrentPrice();
            if (price <= maxPrice) {
                throw new ApiException(Integer.parseInt(ApiConstants.CODE_PRICE_ERROR),
                        "Current price is higher than  bid price");
            }
        }
        Double bidPrice = price;

        if (null != temp.getBidPrice()) {
            price = price - temp.getBidPrice();
        }



        price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        Properties configuration = appClient.getConfiguration();
        DBaseApiService dBaseApiService = appClient.getdBaseApiService();

        String accessToken = appClient.getAccessToken();
        String tradeNo = GenerateNoUtil.generateTradeNo();
        PayRequest payRequest = new PayRequest();
        payRequest.setAmount(new BigDecimal(price));
        // 支付结果提醒url 必填
        payRequest.setNotifyUrl(configuration.getProperty(ApiConstants.DA_NOTIFY_URL));
        payRequest.setUserNo(userId);
        payRequest.setDetail("");
        payRequest.setOrderDetailUrl(configuration.getProperty(ApiConstants.DA_DETAIL_URL));
        payRequest.setTitle("竞拍");
        payRequest.setTradeNo(tradeNo);
        payRequest.setRedirect_url(configuration.getProperty(ApiConstants.DA_DETAIL_URL));

        String payUrl = dBaseApiService
                .doOrder(configuration.getProperty(ApiConstants.DA_APPID), accessToken, payRequest,
                        appClient);

        History history = new History();
        history.setTradeNo(tradeNo);
        history.setGoodsId(gId);
        history.setUserId(userId);
        history.setBidPrice(bidPrice);
        history.setBidTime(System.currentTimeMillis());
        history.setPayPrice(price);
        //判断此次竞拍是否支付 0：表示未支付 1：表示支付成功
        history.setTemp("0");
        hRepository.insertHistory(history);


        obj.put("flag", true);
        obj.put("pay_url", payUrl);
        return obj;
    }


    @Transactional(rollbackFor = Exception.class)
    public String doPaySuccessed(History history, Double price, PayNotifyBean notifyBean,
            boolean flag) throws ApiException, ParseException {

        Properties configuration = appClient.getConfiguration();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long date = format.parse(notifyBean.getPayTime()).getTime();
        Transaction transaction = new Transaction();
        transaction.setGoodsId(history.getGoodsId());
        String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
        if (flag) {

            //业务查询
            //插入交易到本地数据库
            transaction.setFromUserId(history.getUserId());
            transaction.setPrice(price);
            transaction.setToUserId(transferId);
            transaction.setTxId(notifyBean.getBusinessNo());
            transaction.setTxTime(date);
            transaction.setTemp("1");
            tRepository.insertTransaction(transaction);

            //当支付时间超过商品竞拍截止时间则直接发起退款
            if (date > history.getEndTime()) {
                //设置此交易为无效交易
                hRepository.updateHistory("1", null, "3",null, notifyBean.getTradeNo());

                String accessToken = appClient.getAccessToken();
                DBaseApiService dBaseApiService = appClient.getdBaseApiService();
                List<BusinessRecord> businessRecords = new ArrayList<>();
                BusinessRecord businessRecord = new BusinessRecord();
                businessRecord.setUserNo(history.getUserId());
                businessRecord.setAmount(new BigDecimal(price));
                businessRecord.setNotifyUrl(configuration.getProperty(ApiConstants.DA_ISSUE_NOTIFY_URL));
                businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
                businessRecords.add(businessRecord);
                try {
                    //下发
                    dBaseApiService.doIssue(
                            appClient.getConfiguration().getProperty(ApiConstants.DA_APPID),
                            accessToken, businessRecords,
                            new TypeToken<RestApiResponse<Map<String, String>>>() {
                            }, appClient).getData();

                    return "SUCCESS";
                } catch (ApiException e) {
                    if (String.valueOf(e.getStatusCode())
                            .equals(ApiConstants.CODE_INSUFFICIENT_BALANCE)) {
                        //余额不足 普获 不做处理 定时任务会处理
                    } else {
                        throw e;
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                    return "FAILED";
                }

            } else {
                //进入该方法表示用户已经支付成功，因此更新交易状态为1即已经支付
                LOGGER.debug("start update table t_history,tradeNo: {}", notifyBean.getTradeNo());
                hRepository.updateHistory("1", null, "1",null,notifyBean.getTradeNo());
                LOGGER.debug("finnish update table t_history,tradeNo: {}", notifyBean.getTradeNo());
            }

            //同步
            synchronized (this) {
                Double maxPrice = hRepository.findMaxPriceByGid(history.getGoodsId());
                //当用户1、用户2同时进入支付界面时此时是在灵光币平台本系统无法得知那个支付的金额大因此需要进行验证
                //验证 当支付金额是最大值时更新当前商品竞拍价格
                if (history.getBidPrice() >= maxPrice) {
                    Goods goods = new Goods();
                    goods.setGoodsId(history.getGoodsId());
                    goods.setCurrentPrice(history.getBidPrice());

                    goodsRepository.updateGoods(goods);
                }
            }
        } else {
            //进入该方法表示用户已经支付成功，因此退款
            LOGGER.debug("start update table t_history,tradeNo: {}", notifyBean.getTradeNo());
            hRepository.updateHistory(null, "1", "1",null, notifyBean.getTradeNo());
            LOGGER.debug("finnish update table t_history,tradeNo: {}", notifyBean.getTradeNo());

            transaction.setFromUserId(transferId);
            transaction.setPrice(price);
            transaction.setToUserId(history.getUserId());
            transaction.setTxId(notifyBean.getBusinessNo());
            transaction.setTxTime(date);
            tRepository.insertTransaction(transaction);
        }
        return "SUCCESS";

    }

    /**
     * 竞拍完成时定时任务调用
     */
    public void bidCompleted(String gId, String sellerId) throws IOException {

        //查询处
        List<History> historyList = hRepository
                .findBidHistoryByGoodsId(gId);
        Goods goods = new Goods();
        goods.setGoodsId(gId);
        if (null != historyList && historyList.size() > 0) {
            //设置商品完成
            goods.setStatus(2);
            goodsRepository.updateGoods(goods);
            //推迟5分钟下发
            try {
                Thread.sleep(1000*60*5L);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            bidCompletedMethod(historyList, gId, sellerId, historyList.get(0).getCurrentPrice());
        }else{
            //设置商品流拍
            goods.setStatus(3);
            goodsRepository.updateGoods(goods);
            msgRepository.insertMessage(sellerId, gId, '2', '0');
        }
    }

    public void bidCompletedMethod(List<History> historyList, String gId, String sellerId,
            Double currentPrice) throws IOException {

        Goods goods = new Goods();
        goods.setGoodsId(gId);

        //没人竞拍
        if (historyList.size() <= 0) {
            goods.setStatus(3);
            goodsRepository.updateGoods(goods);
            msgRepository.insertMessage(sellerId, gId, '2', '0');
            return;
        }

        Properties configuration = appClient.getConfiguration();
        DBaseApiService dBaseApiService = appClient.getdBaseApiService();

        TypeToken<RestApiResponse<List<Map<String, String>>>> typeToken = new TypeToken<RestApiResponse<List<Map<String, String>>>>() {
        };
        String accessToken = appClient.getAccessToken();
        //判断是否是买家
        boolean flag = true;
        boolean flag1 = true;
        List<BusinessRecord> businessRecords = new ArrayList<>();
        for (int i = 0; i < historyList.size(); i++) {
            History history = historyList.get(i);
            if(flag1&&null == history.getCurrentPrice()){
                Double maxPrice = hRepository.findMaxPriceByGid(history.getGoodsId());
                if(history.getBidPrice().equals(maxPrice)){
                    currentPrice = maxPrice;
                    flag1 = false;
                }
            }
            BusinessRecord businessRecord = new BusinessRecord();
            //查看这个用户这件商品是否下发过
            String isIssue = history.getIsIssue();
            businessRecord.setUserNo(history.getUserId());
            if(null !=isIssue && isIssue.equals("1")) {
                businessRecord.setAmount(new BigDecimal(history.getPayPrice()));
            }else if(null == isIssue || isIssue.equals("0")){
                businessRecord.setAmount(new BigDecimal(history.getBidPrice()));
            }
            businessRecord.setNotifyUrl(configuration.getProperty(ApiConstants.DA_ISSUE_NOTIFY_URL));
            String issueTradeNo = history.getIssueTradeNo();
            if(Strings.isNullOrEmpty(issueTradeNo)) {
                businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
            }else{
                businessRecord.setTradeNo(issueTradeNo);
            }
            if (true == flag && history.getBidPrice().equals(currentPrice)) {
                if(null ==history.getBuyerId() || history.getBuyerId().equals("0")) {
                    //拍卖成功
                    msgRepository.insertMessage(sellerId, gId, '1', '0');
                    //竞拍成功
                    msgRepository.insertMessage(history.getUserId(), gId, '3', '0');
                    goods.setBuyerId(history.getUserId());
                    goods.setStatus(2);
                    businessRecord.setUserNo(sellerId);
                }
                //防止 当商品信息更新了，但是下发失败
                if(history.getUserId().equals(history.getBuyerId())){
                    businessRecord.setUserNo(sellerId);
                }
                flag = false;
            } else {
                //竞拍失败
                if(null ==history.getBuyerId() || history.getBuyerId().equals("0")) {
                    msgRepository.insertMessage(history.getUserId(), gId, '4', '0');
                }
            }

            if(Strings.isNullOrEmpty(issueTradeNo)) {
                hRepository.updateHistory(null,null,null,businessRecord.getTradeNo(),history.getTradeNo());
            }else{
                Map<String,Object> data = doQueryTxStatus(issueTradeNo);

                if (data.get("status").equals("0")) {
                    hRepository.updateHistory(null, "1", null,null, history.getTradeNo());

                    String txId = String.valueOf(data.get("business_no"));
                    Integer count = tRepository.checkTx(txId);
                    if(count == 0) {
                        Transaction transaction = new Transaction();
                        String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
                        transaction.setGoodsId(history.getGoodsId());
                        transaction.setFromUserId(transferId);
                        transaction.setPrice(Double.parseDouble(data.get("amount").toString()));
                        transaction.setToUserId(businessRecord.getUserNo());
                        transaction.setTxId(txId);
                        transaction.setTxTime(Long.parseLong(data.get("last_time").toString()));
                        transaction.setTemp("1");
                        tRepository.insertTransaction(transaction);
                    }
                //交易不存在
                } else if (data.get("status").equals("2")) {
                    //businessRecord.setTradeNo(GenerateNoUtil.generateTradeNo());
                    //hRepository.updateHistory(null,null,null,businessRecord.getTradeNo(),history.getTradeNo());
                    businessRecords.add(businessRecord);
                }
            }

        }
        if(null ==historyList.get(0).getBuyerId() || historyList.get(0).getBuyerId().equals("0")) {
            if(!Strings.isNullOrEmpty(goods.getBuyerId())) {
                goodsRepository.updateGoods(goods);
            }
        }
        if(businessRecords.size()>0) {
            dBaseApiService
                    .doIssue(appClient.getConfiguration().getProperty(ApiConstants.DA_APPID),
                            accessToken, businessRecords, typeToken, appClient).getData();
        }

    }

    /**
     * 查询未退款交易并根据商品id进行分类 一个商品一个list
     */
    public List<List<History>> findHistoryForNoIssueOrder() {
        //多加10分钟 执行
        Long endTime = System.currentTimeMillis() + 1000*60*10L;
        //没有数据返回的结果为list,第一个元素为Null
        List<History> list = hRepository.findTransactionForNoIssueOrder(endTime);
        if (null != list) {
            list.removeAll(Collections.singleton(null));
        }
        List<List<History>> result = null;
        if (null != list && !list.isEmpty()) {
            result = new ArrayList<>();
            Set<String> set = new HashSet<String>();
            List<History> hList = null;
            for (int i = 0; i < list.size(); i++) {
                History temp = list.get(i);
                LOGGER.info("History:{}", temp);

                if (!set.contains(temp.getGoodsId())) {
                    set.add(temp.getGoodsId());
                    if (null != hList && hList.size() > 0) {
                        result.add(hList);
                    }
                    hList = new ArrayList<>();
                }

                hList.add(temp);

                if (i == list.size() - 1) {
                    result.add(hList);
                }

            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 查询定时任务退款失败的交易
     */
    public void checkNoPayTx() throws IOException {
        List<History> list = hRepository.checkNoPayTx();
        list.removeAll(Collections.singleton(null));
        for (History history : list) {
            Map<String,Object> data = doQueryTxStatus(history.getTradeNo());
            //表示交易已经完成
            if (data.get("status").equals("0")) {
                hRepository.updateHistory("1", null, "1",null, history.getTradeNo());
                String txId = String.valueOf(data.get("business_no"));
                Integer count = tRepository.checkTx(txId);
                if(count == 0) {
                    Transaction transaction = new Transaction();
                    String transferId = appClient.getConfiguration().getProperty(ApiConstants.DA_APPID);
                    transaction.setGoodsId(history.getGoodsId());
                    transaction.setFromUserId(history.getUserId());
                    transaction.setPrice(Double.parseDouble(data.get("amount").toString()));
                    transaction.setToUserId(transferId);
                    transaction.setTxId(String.valueOf(data.get("business_no")));
                    transaction.setTxTime(Long.parseLong(data.get("last_time").toString()));
                    transaction.setTemp("1");
                    tRepository.insertTransaction(transaction);
                }
            } else if (data.get("status").equals("1")) {
                if (history.getIsValid().equals("2")) {
                    hRepository.updateHistory(null, null, "3",null, history.getTradeNo());
                } else {
                    hRepository.updateHistory(null, null, "2",null, history.getTradeNo());
                }
            } else if (data.get("status").equals("2")) {
                hRepository.updateHistory(null, null, "3",null, history.getTradeNo());
            }
        }

    }
}
