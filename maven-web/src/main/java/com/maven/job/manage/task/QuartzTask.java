package com.maven.job.manage.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务类
 * 
 * @author chenjian
 * @createDate 2019-10-15
 */
public class QuartzTask implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		// 执行任务
		JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		TaskData quartzTask = (TaskData) dataMap.get("task");
		// 调用接口函数
		quartzTask.execute();
	}
}