package com.aution.dapp.server.quartz;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.DappService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 主要解决灵光云支付成功回调失败问题
 * @author hewensheng
 */
@Component
public class CheckJob implements Job{

	private static final Log LOG = LogFactory.getLog(CheckJob.class);
	
	@Autowired
	private DappService dappService;

	
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try {
			LOG.debug("完成执行检查任务");
			dappService.checkNoPayTx();
			LOG.debug("完成执行检查任务");
		}catch (IOException e) {
			LOG.error("IOException："+e.getMessage());
		}
	}
	
	

}
