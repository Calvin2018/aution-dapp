package com.aution.dapp.server.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author hewensheng
 */
@Component
@Order(value = 1)
public class QuartzIssueRunner implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzIssueRunner.class);
	
	@Autowired
	private  Scheduler scheduler;
	@Value("${quartz.job.issue.cronExpression}")
	private  String issueCronExpression;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		 //创建任务
	    JobDetail jobDetail = JobBuilder.newJob(NoIssueJob.class).withIdentity(NoIssueJob.class.getName(),"NoIssueJob").build();
	    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(issueCronExpression);
	    //创建任务触发器
	    TriggerKey key = TriggerKey.triggerKey(NoIssueJob.class.getName(),"NoIssueJob");
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
	    //将触发器与任务绑定到调度器内
	    try {
			if (!scheduler.checkExists(key)) {
			  LOGGER.info("Schedule job - with key {} , and expression {}", key, issueCronExpression);
			  scheduler.scheduleJob(jobDetail, trigger);
			  scheduler.start();
			}
		} catch (SchedulerException e) {
			  LOGGER.info("SchedulerException:{}",e.getMessage());
		}
		
	}
}