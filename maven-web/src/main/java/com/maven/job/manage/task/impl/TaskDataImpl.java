package com.maven.job.manage.task.impl;

import java.util.Calendar;

import com.maven.job.manage.task.TaskData;

/**
 * 测试任务实现类
 * 
 * @author chenjian
 * @createDate 2019-10-15
 */
public class TaskDataImpl implements TaskData {

	@Override
	public void execute() {
		System.out.println(Calendar.getInstance().getTime() + "/Test success");
	}
}