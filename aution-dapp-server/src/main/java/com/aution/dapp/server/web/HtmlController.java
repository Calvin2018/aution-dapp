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
	public String showPaySuccessedPage(@RequestParam("trade_no")String tradeNo,@RequestParam("coin_trade_no")String coinTradeNo) {
		LOGGER.info("查看详情");
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
	public  String getCode(@RequestParam("code")String code, @RequestParam("state")String state, HttpServletRequest request) throws IOException {

		String dappState = appClient.getConfiguration().getProperty(ApiConstants.DA_STATE);
		if(!state.equals(dappState)){
			return "redirect:unauthorized.html";
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
		

		return "redirect:index.html";
		
	}
	private static final Log LOG = LogFactory.getLog(HtmlController.class);
	@RequestMapping(value = "/test",method = RequestMethod.GET)
	public void test(){
		try {
			//1、job
			LOG.debug("开始执行退款");
			List<List<History>> list = dappService.findHistoryForNoIssueOrder();
			if(null != list) {
				list.removeAll(Collections.singleton(null));
			}
			if(null == list || list.size() == 0) {
				LOG.debug("未找到需要退款记录");
				return ;
			}
			for(List<History> temp:list) {

				dappService.bidCompletedMethod(temp, temp.get(0).getGoodsId(), temp.get(0).getSellerId(),temp.get(0).getCurrentPrice());
			}
			LOG.debug("完成执行退款操作");
		}catch(ApiException e) {
			if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_INSUFFICIENT_BALANCE)) {
				LOG.debug("定时任务NoIssueJob下发失败,账号余额不足");
			}
		}catch (IOException e) {
			LOG.error("IOException："+e.getMessage());
		}
	}

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public void test1(){
       String test = "_body=[{\"user_no\":\"10012019102102386143256887992328\",\"trade_no\":\"aa055b1f-b26c-471c-9d5a-b7ac554a72da\",\"amount\":2,\"notify_url\":\"http://172.16.214.200:8080/dapp/api/order/issue/notify\"}]&access_token=fd0a3c52c0ed4c5ea5a07860883cb0e5&appid=xft98nu5rp8px848dqbwyw5liiz&appsecret=f5bd32e2e43c4667b081a5401e85a4c8&timestamp=1577709344";
       String sign =  DigestUtils.sha1Hex(test);
       System.out.println(sign);
        Map<String, String> signParam = new LinkedHashMap<>();
        signParam.put("_body", "[{\"user_no\":\"10012019102102386143256887992328\",\"trade_no\":\"aa055b1f-b26c-471c-9d5a-b7ac554a72da\",\"amount\":2,\"notify_url\":\"http://172.16.214.200:8080/dapp/api/order/issue/notify\"}]");
        signParam.put("access_token", "fd0a3c52c0ed4c5ea5a07860883cb0e5");
        signParam.put("appid", "xft98nu5rp8px848dqbwyw5liiz");
        signParam.put("appsecret", "f5bd32e2e43c4667b081a5401e85a4c8");
        signParam.put("timestamp", "1577709344");
        String sign1 = SignUtil.createCommonSign(signParam);
        System.out.println(sign1);
        System.out.println(sign.equals(sign1));
    }

	
}
