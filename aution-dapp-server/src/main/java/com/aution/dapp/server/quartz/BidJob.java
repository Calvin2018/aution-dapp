package com.aution.dapp.server.quartz;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aution.dapp.server.service.DappService;
import com.google.common.base.Strings;

@Component
public class BidJob implements Job{

	private static final Log LOG = LogFactory.getLog(BidJob.class);
	
	@Autowired
	private DappService dappService;

	
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String goodsId = jobDataMap.getString("goodsId");
		String userId = jobDataMap.getString("userId");
		if(Strings.isNullOrEmpty(goodsId) || Strings.isNullOrEmpty(userId)) {
			LOG.debug("Arguments goodsId and userId can't be null");
			return;
		}
		try {
			LOG.debug("开始执行竞拍结束操作");
			dappService.bidCompleted(goodsId, userId);
			LOG.debug("完成执行竞拍结束操作");
		} catch (IOException e) {
			LOG.error("IOException");
		}
	}
	
	

}
