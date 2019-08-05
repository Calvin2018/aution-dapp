package com.aution.dapp.server.web;



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

import javax.servlet.http.HttpServletRequest;



@Controller
public class HtmlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
	@Autowired
	private HistoryService historyService;
	@Autowired
	private DappService dappService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="/api/order/pay/successed")
	public String showPaySuccessedPage(@RequestParam("trade_no")String tradeNo,@RequestParam("coin_trade_no")String coinTradeNo) {
		LOGGER.debug("start update table t_history,tradeNo: {}",tradeNo);
		String temp = "1";
		historyService.updateHistory(temp,tradeNo);
		LOGGER.debug("finnish update table t_history,tradeNo: {}",tradeNo);
		
		History history = historyService.findHistoryByTradeNo(tradeNo);
		Double maxPrice = historyService.findMaxPriceByGid(history.getGoodsId());
		if(history.getBidPrice() >= maxPrice) {
			Goods goods = new Goods();
			goods.setGoodsId(history.getGoodsId());
			goods.setCurrentPrice(history.getBidPrice());
		
			goodsService.updateGoods(goods);
		}
		return "redirect:/index.html";
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public  String echo(@RequestParam("access_token")String accessToken,HttpServletRequest request) throws IOException {
		LOGGER.info("access_token: {}",accessToken);
		Map<String,String> temp = dappService.getUserInfo(accessToken);
		String userId = temp.get("job_number");
		String avatar = temp.get("avatar");
		String userName = temp.get("user_name");
		String userPhone = temp.get("user_phone");
		boolean flag = goodsService.insertUser(userId, avatar, userName,userPhone);
		if(!flag)goodsService .updateUser(userId, avatar, userName, userPhone);
		
		request.getSession().setAttribute("job_number", userId);
		/*request.getSession().setAttribute("avatar", avatar);
		request.getSession().setAttribute("user_name", userName);
		request.getSession().setAttribute("user_phone", userPhone);*/
		
		return "redirect:index.html";
		
	} 
	
	
	
	
}
