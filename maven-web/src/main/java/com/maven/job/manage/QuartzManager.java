package com.maven.job.manage;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;
import com.maven.job.manage.task.TaskData;

/**
 * 任务管理类
 * 
 * @author chenjian
 * @createDate 2019-10-15
 */
public class QuartzManager {

	private Logger logger = LoggerFactory.getLogger(QuartzManager.class);

	// 分隔符
	private final String SEPARATOR = "_";

	// 任务组名
	private final String GROUP_NAME = "group";

	// 触发器名
	private final String TRIGGER_NAME = "trigger";

	// 触发器组名
	private final String TRIGGER_GROUP_NAME = "groupTrigger";

	// 修改方式
	public final int UPDATE_MODE = 1;

	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 添加任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param cron
	 *            时间设置
	 * @param quartzTask
	 *            task任务
	 * @return
	 */
	public boolean addJob(String jobName, Class<? extends Job> jobClass,
			String cron, TaskData quartzTask) {
		return addJob(jobName, jobName + SEPARATOR + GROUP_NAME, jobName
				+ SEPARATOR + TRIGGER_NAME, jobName + SEPARATOR
				+ TRIGGER_GROUP_NAME, jobClass, cron, quartzTask);
	}

	/**
	 * 添加任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param cron
	 *            时间设置
	 * @param quartzTask
	 *            task任务
	 */
	public boolean addJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, String cron, TaskData quartzTask) {
		boolean result = false;

		try {
			JobDataMap jobMap = new JobDataMap();
			jobMap.put("task", quartzTask);

			// 任务名、任务组、任务执行类
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(jobName, jobGroupName).usingJobData(jobMap)
					.build();

			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
					.newTrigger();

			// 触发器名、触发器组
			triggerBuilder.withIdentity(triggerName, triggerGroupName);
			triggerBuilder.startNow();

			// 触发器时间设定
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));

			// 创建Trigger对象
			CronTrigger trigger = (CronTrigger) triggerBuilder.build();

			// 调度容器设置JobDetail和Trigger
			scheduler.scheduleJob(jobDetail, trigger);

			// 启动任务
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

			result = true;
			logger.info("Quartz job add success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Quartz job add error");
		}

		return result;
	}

	/**
	 * 修改任务触发时间
	 * 
	 * @param jobName
	 *            任务名
	 * @param cron
	 *            时间设置
	 * @return
	 */
	public boolean updateJobTime(String jobName, String cron, int mode) {
		// 判断用哪种方式修改
		if (mode == UPDATE_MODE) {
			return updateJobTime(jobName, jobName + SEPARATOR + GROUP_NAME,
					jobName + SEPARATOR + TRIGGER_NAME, jobName + SEPARATOR
							+ TRIGGER_GROUP_NAME, cron);
		} else {
			return updateJobTime(jobName + SEPARATOR + TRIGGER_NAME, jobName
					+ SEPARATOR + TRIGGER_GROUP_NAME, cron);
		}
	}

	/**
	 * 修改任务触发时间
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param cron
	 *            时间设置
	 * @return
	 */
	public boolean updateJobTime(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName, String cron) {
		boolean result = false;

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,
					triggerGroupName);

			// 判断任务是否存在
			if (StringUtils.isNotEmpty(triggerKey)) {
				CronTrigger trigger = (CronTrigger) scheduler
						.getTrigger(triggerKey);

				// 判断触发器是否存在
				if (StringUtils.isNotEmpty(trigger)) {
					String oldTime = trigger.getCronExpression();

					// 判断任务时间是否一致
					if (!oldTime.equalsIgnoreCase(cron)) {
						JobDetail jobDetail = scheduler.getJobDetail(JobKey
								.jobKey(jobName, jobGroupName));

						Class<? extends Job> jobClass = jobDetail.getJobClass();
						TaskData quartzTask = (TaskData) jobDetail
								.getJobDataMap().get("task");

						// 移除任务
						removeJob(jobName, jobGroupName, triggerName,
								triggerGroupName);

						// 添加任务
						addJob(jobName, jobGroupName, triggerName,
								triggerGroupName, jobClass, cron, quartzTask);
					}

					result = true;
					logger.info("Quartz job time update success");
				}
			} else {
				logger.info("Quartz job not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Quartz job time update error");
		}

		return result;
	}

	/**
	 * 修改任务触发时间
	 * 
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param cron
	 *            时间设置
	 * @return
	 */
	public boolean updateJobTime(String triggerName, String triggerGroupName,
			String cron) {
		boolean result = false;

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,
					triggerGroupName);

			// 判断任务是否存在
			if (StringUtils.isNotEmpty(triggerKey)) {
				CronTrigger trigger = (CronTrigger) scheduler
						.getTrigger(triggerKey);

				// 判断触发器是否存在
				if (StringUtils.isNotEmpty(trigger)) {
					String oldTime = trigger.getCronExpression();

					// 判断任务时间是否一致
					if (!oldTime.equalsIgnoreCase(cron)) {
						// 触发器
						TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
								.newTrigger();

						// 触发器名、触发器组
						triggerBuilder.withIdentity(triggerName,
								triggerGroupName);
						triggerBuilder.startNow();

						// 触发器时间设定
						triggerBuilder.withSchedule(CronScheduleBuilder
								.cronSchedule(cron));

						// 创建Trigger对象
						trigger = (CronTrigger) triggerBuilder.build();

						scheduler.rescheduleJob(triggerKey, trigger);
					}

					result = true;
					logger.info("Quartz job time update success");
				}
			} else {
				logger.info("Quartz job not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Quartz job time update error");
		}

		return result;
	}

	/**
	 * 移除任务
	 * 
	 * @param jobName
	 *            任务名
	 * @return
	 */
	public boolean removeJob(String jobName) {
		return removeJob(jobName, jobName + SEPARATOR + GROUP_NAME, jobName
				+ SEPARATOR + TRIGGER_NAME, jobName + SEPARATOR
				+ TRIGGER_GROUP_NAME);
	}

	/**
	 * 移除任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 */
	public boolean removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName) {
		boolean result = false;

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,
					triggerGroupName);

			// 判断任务是否存在
			if (StringUtils.isNotEmpty(triggerKey)) {
				// 停止触发器
				scheduler.pauseTrigger(triggerKey);

				// 移除触发器
				scheduler.unscheduleJob(triggerKey);

				// 删除任务
				scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));

				result = true;
				logger.info("Quartz job remove success");
			} else {
				logger.info("Quartz job not exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Quartz job remove error");
		}

		return result;
	}

	/**
	 * 启动所有定时任务
	 */
	public void startJobs() {
		try {
			scheduler.start();

			logger.error("Scheduler start success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Scheduler start error");
		}
	}

	/**
	 * 挂起所有定时任务
	 */
	public void standbyJobs() {
		try {
			scheduler.standby();

			logger.error("Scheduler standby success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Scheduler standby error");
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public void shutdownJobs() {
		shutdownJobs(false);
	}

	/**
	 * 关闭所有定时任务
	 * 
	 * @param wait
	 *            是否等待Scheduler执行完后关闭(true:表示等待,false:表示立即关闭)
	 */
	public void shutdownJobs(boolean wait) {
		try {
			// 判断Scheduler是否已关闭
			if (!scheduler.isShutdown()) {
				scheduler.shutdown(wait);

				logger.error("Scheduler shutdown success");
			} else {
				logger.error("Scheduler is shutdown");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Scheduler shutdown error");
		}
	}
}