package com.aution.dapp.server.core.schedule;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aution.dapp.server.service.DappService;

//使用quartz
public class MyTimeTask extends TimerTask{

	private static final Log LOG = LogFactory.getLog(MyTimeTask.class);
	
	private DappService dappService;
	private String userId;
	private String gId;
	
	
	public MyTimeTask(DappService dappService, String userId, String gId) {
		super();
		this.dappService = dappService;
		this.userId = userId;
		this.gId = gId;
	}


	@Override
	public void run() {
		try {
			dappService.bidCompleted(gId, userId);
		} catch (IOException e) {
			LOG.error("IOException");
		}
		
	}
	
	

}
