package com.aution.dapp.server.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.RestApiResponse;
import com.aution.dapp.server.core.internal.DBaseApiService;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.model.Transaction;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import com.aution.dapp.server.service.HistoryService;
import com.aution.dapp.server.service.TransactionService;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

@RestController
public class SystemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private DappService dappService;
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="/api/system/transaction/echo",method=RequestMethod.GET)
	@ResponseBody
	public List<Transaction>  echoTra(String gId,String userId,Double price) throws Exception {
		return transactionService.findAllTransactionByTimeSort("DESC");
	}
	@RequestMapping(value="/api/system/history/echo",method=RequestMethod.GET)
	@ResponseBody
	public List<History>  echoHis(String gId,String userId,Double price) throws Exception {
		return historyService.findAllHistory();
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public  String echo(String access_token) throws IOException {
		System.out.println(access_token);
		
		Map<String,String> test = dappService.getUserInfo(access_token);
		String userId = test.get("job_number");
		String avatar = test.get("avatar");
		String userName = test.get("user_name");
		String userPhone = test.get("user_phone");
		goodsService.insertUser(userId, avatar, userName,userPhone);
		JSONObject obj = dappService.getBalance(userId, "100", null);
		String balance = (String)obj.get("balance");
		System.out.println(userId);
		return access_token;
		
	} 
	@RequestMapping(value="/test",method=RequestMethod.GET)
	@ResponseBody
	public String test() throws IOException {
		return dappService.issueTest();
	}
	@RequestMapping(value="/getUserInfo",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> getUserInfo(String accessToken) throws IOException{
		
		return dappService.getUserInfo(accessToken);
	}
	
	
}
