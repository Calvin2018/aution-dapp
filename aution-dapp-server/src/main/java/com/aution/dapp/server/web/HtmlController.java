package com.aution.dapp.server.web;



import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



@Controller
public class HtmlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
	@Autowired
	private DappService dappService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="/api/order/pay/successed")
	public String showPaySuccessedPage(@RequestParam("trade_no")String tradeNo,@RequestParam("coin_trade_no")String coinTradeNo) {
		LOGGER.info("查看详情");
		return "redirect:/index.html";
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	public  String login(@RequestParam("access_token")String accessToken) throws IOException {
		LOGGER.info("access_token: {}",accessToken);
		Map<String,String> temp = dappService.getUserInfo(accessToken);

		String userId = temp.get("job_number");
		String avatar = temp.get("avatar");
		String userName = temp.get("user_name");
		String userPhone = temp.get("user_phone");

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userId,accessToken);
		subject.login(token);

		//不存在则插入
		boolean flag = goodsService.insertUser(userId, avatar, userName,userPhone);
		if(!flag) {
			goodsService .updateUser(userId, avatar, userName, userPhone);
		}
		

		return "redirect:index.html";
		
	} 
	
	
	
	
}
