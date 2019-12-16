package com.aution.dapp.server.web;



import java.io.IOException;
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

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.ApiResult;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.model.Transaction;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.HistoryService;
import com.aution.dapp.server.service.TransactionService;
import com.google.common.base.Strings;


@RestController
public class SystemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private DappService dappService;
	
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
	
	@RequestMapping(value="/api/getUserInfo",method=RequestMethod.POST)
	public ApiResult<Map<String,String>> getToken(HttpServletRequest request) throws ApiException, IOException  {
		String userId = (String)request.getSession().getAttribute("job_number");
		
		ApiResult<Map<String,String>> result = new ApiResult<Map<String,String>>();
		Map<String,String> map = null;
		try {
			
			if(!Strings.isNullOrEmpty(userId)) {
				map =  dappService.getUserInfoUserId(userId);
			}
			
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(map);
			
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
	
}
