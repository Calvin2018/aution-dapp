package com.aution.dapp.server.web;



import com.aution.dapp.server.config.MyUsernamePasswordToken;
import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.model.ShiroUser;
import com.aution.dapp.server.utils.HttpUtils;
import com.google.common.base.Strings;
import net.sf.json.JSONObject;
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
import org.springframework.web.bind.annotation.*;

import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	@RequestMapping(value="/login",method=RequestMethod.GET)
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

//	private Map<String,String> test = new TreeMap<>();
//	private Map<String,String> codes = new TreeMap<>();

	@RequestMapping(value="/scoin",method=RequestMethod.GET)
	public  String getCode(@RequestParam("code")String code, @RequestParam("state")String state,HttpServletRequest req) throws IOException {


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		//code 只能使用一次
		String dappState = appClient.getConfiguration().getProperty(ApiConstants.DA_STATE);
		if(!state.equals(dappState)){
			return "redirect:/unauthorized.html";
		}
		String ip = HttpUtils.getIpAddress(req);
		LOGGER.info("ip:{},code: {}",ip,code);
//		String time =  sdf.format(new Date(System.currentTimeMillis()));
//		codes.put(time,code+";"+ip);

		Map<String, String> temp = null;

		try {
			 temp = dappService.getUserInfo(code);
		}catch(ApiException e){
			//access_token 失效则重新获取code
			LOGGER.info("code: {},msg:	{}",e.getStatusCode(),"获取用户信息异常导致重定向到登录页面");
			return "redirect:/login";
		}

		String userNo = temp.get("user_no");
		String avatar = temp.get("avatar");
		String userName = temp.get("user_name");
		String userPhone = temp.get("user_phone");

		LOGGER.info("code: {},user_no:{},user_name:{},ip:{}",code,userNo,userName,ip);
		String info = "code: "+code+",user_no:"+userNo+",user_name:"+userName+",ip:"+ip;

//		test.put(time,info);


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

//	@RequestMapping("/test")
//	@ResponseBody
//	public JSONObject getTest(){
//		JSONObject obj = JSONObject.fromObject(test);
//		return obj;
//	}
//	@RequestMapping("/codes")
//	@ResponseBody
//	public JSONObject getCodes(){
//		JSONObject obj = JSONObject.fromObject(codes);
//		return obj;
//	}

}
