package com.aution.dapp.server.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.core.message.MsgInMemoryDuplicateChecker;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.model.PayNotifyBean;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.HistoryService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/api/order")
public class DappController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DappController.class);
	
	@Autowired
	private HistoryService historyService;
	@Autowired
	private DappService dappService;
	
	private  AppClient appClient ;
	
	@RequestMapping(value="/getUserInfo",method=RequestMethod.POST)
	public Map<String,String> getUserInfo(String token) throws ApiException, IOException  {
		return dappService.getUserInfo(token);
	}
	
	@RequestMapping(value="/getBalance",method=RequestMethod.POST)
	public JSONObject getBalance(String userId,String amount,String feeAmount) throws ApiException, IOException  {
		return dappService.getBalance(userId,amount,feeAmount);
	}
	
	@RequestMapping(value="/bid",method=RequestMethod.POST)
	public JSONObject bid(String gId,String userId,Double price) throws Exception {
		try {
			return  dappService.createOrder(gId, userId, price);
		} catch (Exception e) {
			LOGGER.debug("create order failed for gId: %s,userId: %s",gId,userId);
			throw new ApiException(e.getMessage());
		}
	}
	@RequestMapping(value="/pay/notify",method=RequestMethod.POST)
	public String notify(String sign, @RequestBody PayNotifyBean notifyBean) throws ApiException, ParseException {
		
		History history = new History();
		history.setTradeNo(notifyBean.getTradeNo());
		history.setTemp("0");
		if(null == notifyBean||notifyBean.getOrderStatus().equals(ApiConstants.DA_SUCCESS)) {
			historyService.updateHistory(history);
			return "FAILED";
		}
		// 1.基于内存的消息去重,分布式环境请自行编写去重实例
        MsgInMemoryDuplicateChecker.getInstance().isDuplicate(notifyBean);
        // 2.验证签名
        Properties configuration = appClient.getConfiguration();
        
        if (notifyBean.createSign(configuration.getProperty(ApiConstants.DA_APPSECRET)).equals(sign)) {
        	LOGGER.info("Signature verification passed for sign: %s",sign);
        } else {
        	historyService.updateHistory(history);
        	LOGGER.error("Signature verification failed for sign: %s",sign);
        	throw new ApiException("Signature verification failed for sign:"+sign);
        }
        // 3.验证订单金额
        List<History> list = historyService.findHistoryByTradeNoAndGidAndPriceSort(notifyBean.getTradeNo());
        Double price = 0d;
        if(list.size() == 0) {
        	historyService.updateHistory(history);
        	throw new ApiException("Amount verification failed for price:" + price);
        }else if(list.size() == 1) {
			price = list.get(0).getBidPrice();
        }else if(list.size() == 2) {
			price = list.get(0).getBidPrice() - list.get(1).getBidPrice();
        }
        if (new BigDecimal(price).equals(notifyBean.getAmount())) {
        	LOGGER.info("Amount verification passed");
        } else {
        	LOGGER.error("Amount verification failed, may be illegal notification for price: %s",price);
        	historyService.updateHistory(history);
        	throw new ApiException("Amount verification failed, may be illegal notification for price:"+price);
        }

        dappService.doPaySuccessed(list.get(0).getGoodsId(), list.get(0).getUserId(), price, notifyBean.getPayTime(), notifyBean.getCoinTradeNo());
        
        return "SUCCESS";
	}
}
