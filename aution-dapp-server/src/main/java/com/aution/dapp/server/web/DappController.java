package com.aution.dapp.server.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.ApiResult;
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
	
	private static AppClient appClient = AppClient.getInstance();
	
	
	
	@RequestMapping(value="/getBalance",method=RequestMethod.POST)
	public ApiResult<JSONObject> getBalance(@RequestParam("userId")String userId,String amount,String feeAmount) throws  IOException  {
		ApiResult<JSONObject> result = new ApiResult<JSONObject>();
		try {
			JSONObject data  = dappService.getBalance(userId,amount,feeAmount);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(data);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}catch(ApiException e) {
			result.setCode(String.valueOf(e.getStatusCode()));
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		
		return result;
	}
	
	@RequestMapping(value="/bid",method=RequestMethod.POST)
	public ApiResult<JSONObject> bid(@RequestParam("gId")String gId, @RequestParam("userId")String userId, @RequestParam("price")Double price) throws Exception {
		ApiResult<JSONObject> result = new ApiResult<JSONObject>();
		try {
			JSONObject data =  dappService.createOrder(gId, userId, price);
			Boolean flag = (Boolean)data.get("flag");
			if(flag) {
				result.setCode(ApiConstants.CODE_SUCCESS);
			}else {
				result.setCode(ApiConstants.CODE_TIMT_OUT);
			}
			result.setData(data);
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}catch(ApiException e) {
			result.setCode(String.valueOf(e.getStatusCode()));
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;
	}
	@RequestMapping(value="/pay/notify",method=RequestMethod.POST)
	public String notify(String sign, @RequestBody PayNotifyBean notifyBean) throws ApiException, ParseException {
		LOGGER.info("开始notify");
		//String temp = "0";
		
		if(null == notifyBean||notifyBean.getOrderStatus().equals(ApiConstants.DA_SUCCESS)) {
			//historyService.updateHistory(temp,notifyBean.getTradeNo());
			return "FAILED";
		}
		// 1.基于内存的消息去重,分布式环境请自行编写去重实例
        MsgInMemoryDuplicateChecker.getInstance().isDuplicate(notifyBean);
        LOGGER.info("开始验证签名");
        // 2.验证签名
        Properties configuration = appClient.getConfiguration();
        
        if (notifyBean.createSign(configuration.getProperty(ApiConstants.DA_APPSECRET)).equals(sign)) {
        	LOGGER.info("Signature verification passed for sign:{}",sign);
        } else {
        	//historyService.updateHistory(temp,notifyBean.getTradeNo());
        	LOGGER.error("Signature verification failed for sign:{}",sign);
        	return "Signature verification failed for sign:"+sign;
        }
        LOGGER.info("结束验证签名");
        // 3.验证订单金额
        LOGGER.info("开始订单金额");
        List<History> list = historyService.findHistoryByTradeNoAndGidAndPriceSort(notifyBean.getTradeNo());
        Double price = 0d;
        if(null == list || list.size() == 0) {
        	//historyService.updateHistory(temp,notifyBean.getTradeNo());
        	return "Amount verification failed for price:" + price;
        }else if(list.size() == 1) {
			price = list.get(0).getBidPrice();
        }else if(list.size() == 2) {
			price = list.get(0).getBidPrice() - list.get(1).getBidPrice();
        }
        if (new BigDecimal(price).equals(notifyBean.getAmount())) {
        	LOGGER.info("Amount verification passed");
        } else {
        	LOGGER.error("Amount verification failed, may be illegal notification for price:{}",price);
        	//historyService.updateHistory(temp,notifyBean.getTradeNo());
        	return "Amount verification failed, may be illegal notification for price:"+price;
        }
        LOGGER.info("校验成功");
        
        History history = list.get(0);
        
       
        dappService.doPaySuccessed(history, price, notifyBean);
        
        return "SUCCESS";
	}
}
