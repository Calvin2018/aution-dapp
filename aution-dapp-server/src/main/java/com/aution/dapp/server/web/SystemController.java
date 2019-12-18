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

	
	@RequestMapping(value="/api/system/transaction/echo",method=RequestMethod.GET)
	@ResponseBody
	public List<Transaction>  echoTra(String gId,String userId,Double price,Integer page,Integer size) throws Exception {

		return transactionService.findAllTransactionByTimeSort("DESC",page,size);
	}
	@RequestMapping(value="/api/system/history/echo",method=RequestMethod.GET)
	@ResponseBody
	public List<History>  echoHis(String gId,String userId,Double price) throws Exception {
		return historyService.findAllHistory();
	}
	

	
}
