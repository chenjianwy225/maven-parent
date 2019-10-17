package com.maven.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.maven.job.manage.QuartzManager;
import com.maven.service.IBaseService;
import com.maven.service.util.RedisUtil;

/**
 * 基础Controller
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class BaseController {

	@Autowired
	protected IBaseService baseService;

	@Autowired
	protected RedisUtil redisUtil;

	@Autowired
	protected QuartzManager quartzManager;
}