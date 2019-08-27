package com.aution.dapp.server.quartz;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.DappService;

@Component
public class NoIssueJob implements Job{

	private static final Log LOG = LogFactory.getLog(NoIssueJob.class);
	
	@Autowired
	private DappService dappService;

	
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try {
			//1、job
			LOG.debug("开始执行退款");
			List<List<History>> list = dappService.findHistoryForNoIssueOrder();
			if(null != list)
				list.removeAll(Collections.singleton(null));
			if(null == list || list.size() == 0) {
				LOG.debug("未找到需要退款记录");
				LOG.debug("定时任务执行完成");
				return ;
			}
			for(List<History> temp:list) {
				dappService.bidCompletedMethod(temp, temp.get(0).getGoodsId(), temp.get(0).getSellerId(),temp.get(0).getCurrentPrice());
			}
			//2、job
			dappService.findHistoryOfIssueFailed();
			
			
			LOG.debug("完成执行退款操作");
		}catch(ApiException e) {
        	if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_INSUFFICIENT_BALANCE)) {
        		LOG.debug("定时任务NoIssueJob下发失败,账号余额不足");
        	}
        }catch (IOException e) {
			LOG.error("IOException："+e.getMessage());
		}
	}
	
	

}
