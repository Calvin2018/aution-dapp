package com.aution.dapp.server.web;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.HistoryService;

import java.util.Date;


@Controller
public class HtmlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
	@Autowired
	private HistoryService historyService;
	
	@RequestMapping(value="/order/pay/successed")
	public String showPaySuccessedPage(@RequestParam("trade_no")String tradeNo,@RequestParam("coin_trade_no")String coinTradeNo,HttpServletRequest request) {
		LOGGER.debug("start update table t_history,tradeNo: %s",tradeNo);
		History history = new History();
		history.setTradeNo(tradeNo);
		history.setTemp("1");
		history.setBidTime(new Date().getTime());
		historyService.updateHistory(history);
		LOGGER.debug("finnish update table t_history,tradeNo: %s",tradeNo);
		
		history = historyService.findHistoryByPrimaryKey(tradeNo);
		request.setAttribute("goods_id", history.getGoodsId());
		return "index";
	}
	
	
}
