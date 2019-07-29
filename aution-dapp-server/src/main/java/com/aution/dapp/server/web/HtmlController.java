package com.aution.dapp.server.web;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import com.aution.dapp.server.service.HistoryService;

import java.io.IOException;
import java.util.Map;


@Controller
public class HtmlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
	@Autowired
	private HistoryService historyService;
	@Autowired
	private DappService dappService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="/order/pay/successed")
	public String showPaySuccessedPage(@RequestParam("trade_no")String tradeNo,@RequestParam("coin_trade_no")String coinTradeNo) {
		LOGGER.debug("start update table t_history,tradeNo: {}",tradeNo);
		String temp = "1";
		Long time = System.currentTimeMillis();
		historyService.updateHistory(time,temp,tradeNo);
		LOGGER.debug("finnish update table t_history,tradeNo: {}",tradeNo);
		
		History history = historyService.findHistoryByTradeNo(tradeNo);
		
		Goods goods = new Goods();
		goods.setGoodsId(history.getGoodsId());
		goods.setCurrentPrice(history.getBidPrice());
		goodsService.updateGoods(goods);
		
		return "redirect:/index.html?goodsId="+history.getGoodsId();
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public  String echo(@RequestParam("access_token")String accessToken) throws IOException {
		LOGGER.info("access_token: {}",accessToken);
		Map<String,String> test = dappService.getUserInfo(accessToken);
		String userId = test.get("job_number");
		String avatar = test.get("avatar");
		String userName = test.get("user_name");
		String userPhone = test.get("user_phone");
		goodsService.insertUser(userId, avatar, userName,userPhone);
		
		return "redirect:index.html?accessToken="+accessToken;
		
	} 
	
}
