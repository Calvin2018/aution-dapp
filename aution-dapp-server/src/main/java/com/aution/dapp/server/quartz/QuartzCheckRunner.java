package com.aution.dapp.server.quartz;

import org.quartz.*;
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
@Order(value = 2)
public class QuartzCheckRunner implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzCheckRunner.class);
	
	@Autowired
	private  Scheduler scheduler;
	@Value("${quartz.job.check.cronExpression}")
	private  String checkCronExpression;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		 //创建任务
	    JobDetail jobDetail = JobBuilder.newJob(CheckJob.class).withIdentity(CheckJob.class.getName(),"CheckJob").build();
	    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(checkCronExpression);
	    //创建任务触发器
	    TriggerKey key = TriggerKey.triggerKey(CheckJob.class.getName(),"CheckJob");
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
	    //将触发器与任务绑定到调度器内
	    try {
			if (!scheduler.checkExists(key)) {
			  LOGGER.info("Schedule job - with key {} , and expression {}", key, checkCronExpression);
			  scheduler.scheduleJob(jobDetail, trigger);
			  scheduler.start();
			}
		} catch (SchedulerException e) {
			  LOGGER.info("SchedulerException:{}",e.getMessage());
		}
		
	}
}