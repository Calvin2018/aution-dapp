package com.aution.dapp.server.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import com.aution.dapp.server.utils.ShiroSubjectUtils;
import com.google.common.base.Strings;
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
	


	@RequestMapping(value="/bid",method=RequestMethod.POST)
	public ApiResult<JSONObject> bid(@RequestParam("gId")String gId,  @RequestParam("price")Double price) throws Exception {
		ApiResult<JSONObject> result = new ApiResult<JSONObject>();
		try {
			JSONObject data =  dappService.createOrder(gId, ShiroSubjectUtils.getUserNo(), price);
			Boolean flag = (Boolean)data.get("flag");
			if(flag) {
				result.setCode(ApiConstants.CODE_SUCCESS);
			}else {
				result.setCode(ApiConstants.CODE_TIMT_OUT);
			}
			result.setData(data);
			if(null != data.get("msg")) {
				result.setMsg(data.get("msg").toString());
			}
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
	public String notify( @RequestBody PayNotifyBean notifyBean) throws ApiException, ParseException {
		//flag ：true 支付回调 false :下发回调
		return commonNotify(notifyBean,true);
	}
	private String commonNotify(PayNotifyBean notifyBean,boolean flag) throws ApiException, ParseException {
		LOGGER.info("开始notify");

		if(null == notifyBean) {
			throw new IllegalArgumentException("notifyBean is null");
		}
		// 1.基于内存的消息去重,分布式环境请自行编写去重实例
		MsgInMemoryDuplicateChecker.getInstance().isDuplicate(notifyBean);
		LOGGER.info("开始验证签名");


		//交易正在进行
		if(notifyBean.getStatus()==1){
			return "IN TRANSACTION";
			//交易完成
		}else if(notifyBean.getStatus()==0){

			LOGGER.info("结束验证签名");
			// 3.验证订单金额
			LOGGER.info("开始订单金额");
			History history = null;
			if(flag) {
				history = historyService.findHistoryByTradeNoAndGidAndPriceSort(notifyBean.getTradeNo());
                if (null == history) {
                    LOGGER.info("Amount verification failed for price");
                    return "Amount verification failed for price";
                }else{
                    if(history.getTemp().equals("1")){
                        LOGGER.info("Callback already");
                        return "Callback already";
                    }
                }
			}else{
				history = historyService.findHistoryByIssueTradeNo(notifyBean.getTradeNo());
                if (null == history) {
                    LOGGER.info("Amount verification failed for price");
                    return "Amount verification failed for price";
                }else{
                    if(history.getIsValid().equals("1")){
                        LOGGER.info("Callback already");
                        return "Callback already";
                    }
                }
			}
			Double price = 0d;
			if (null == history) {
				return "Amount verification failed for price:" + price;
			} else{
				price = history.getPayPrice();
			}
			if (new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_DOWN).equals(notifyBean.getAmount().setScale(2,BigDecimal.ROUND_HALF_DOWN))) {
				LOGGER.info("Amount verification passed");
			} else {
				LOGGER.error("Amount verification failed, may be illegal notification for price:{}", price);
				return "Amount verification failed, may be illegal notification for price:" + price;
			}
			LOGGER.info("校验成功");

			dappService.doPaySuccessed(history, price, notifyBean,flag);

			return "SUCCESS";
		}else{
			return "FAILED";
		}
	}
	@RequestMapping(value="/issue/single/notify",method=RequestMethod.POST)
	public String issueNotifyForSingle(@RequestBody PayNotifyBean notifyBean) throws ApiException, ParseException {
		return commonNotify(notifyBean,false);
	}
	@RequestMapping(value="/issue/notify",method=RequestMethod.POST)
	public String issueNotify(@RequestBody List<PayNotifyBean> list) throws ApiException, ParseException {
		for(PayNotifyBean notifyBean:list) {
			 commonNotify( notifyBean,false);
		}
		return "SUCCESS";
	}
}
