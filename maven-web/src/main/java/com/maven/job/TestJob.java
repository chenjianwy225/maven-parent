package com.maven.job;

import org.springframework.stereotype.Component;

/**
 * 测试任务类
 * 
 * @author chenjian
 * @createDate 2019-10-15
 */
@Component
public class TestJob {

	public void execute() {
		System.out.println("Test success");
	}
}