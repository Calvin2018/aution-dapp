package com.aution.dapp.server.web;



import com.aution.dapp.server.config.MyUsernamePasswordToken;
import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.model.ShiroUser;
import com.google.common.base.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import com.aution.dapp.server.model.BusinessRecord;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.quartz.NoIssueJob;
import com.aution.dapp.server.utils.SignUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;



@Controller
public class HtmlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
	@Autowired
	private DappService dappService;
	@Autowired
	private GoodsService goodsService;

	private static AppClient appClient = AppClient.getInstance();
	
	@RequestMapping(value="/api/order/pay/successed")
	public String showPaySuccessedPage() {
		LOGGER.info("跳转到首页");
		return "redirect:/index.html";
	}
	@RequestMapping(value="/login")
	public String login() {
		Properties config = appClient.getConfiguration();

		String appId = config.getProperty(ApiConstants.DA_APPID);
		String redirectUrl=config.getProperty(ApiConstants.DA_URL);
		String responseType = ApiConstants.DA_RESPONSE_TYPE;
		String scope = "userinfo";
		String state = config.getProperty(ApiConstants.DA_STATE);
		String authUrl= String.format(config.getProperty(ApiConstants.PROP_COIN_AUTH_URL),appId,redirectUrl,responseType,scope,state);
		return "redirect:" + authUrl;
	}


	@RequestMapping(value="/scoin",method=RequestMethod.GET)
	public  String getCode(@RequestParam("code")String code, @RequestParam("state")String state) throws IOException {

		String dappState = appClient.getConfiguration().getProperty(ApiConstants.DA_STATE);
		if(!state.equals(dappState)){
			return "redirect:/unauthorized.html";
		}

		LOGGER.info("code: {}",code);


		Map<String,String> temp = dappService.getUserInfo(code);

		String userNo = temp.get("user_no");
		String avatar = temp.get("avatar");
		String userName = temp.get("user_name");
		String userPhone = temp.get("user_phone");

		Subject subject = SecurityUtils.getSubject();
		ShiroUser user = new ShiroUser();
		user.setLoginName(userNo);
		user.setPassword(code);
		user.setUserName(userName);
		MyUsernamePasswordToken token = new MyUsernamePasswordToken(user);
		subject.login(token);


		if(null == avatar){
			avatar = "";
		}
		//不存在则插入
		boolean flag = goodsService.insertUser(userNo, avatar, userName,userPhone);
		if(!flag) {
			goodsService .updateUser(userNo, avatar, userName, userPhone);
		}
		

		return "redirect:/index.html";
		
	}
	
}
