package com.maven.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.maven.service.IBaseService;

/**
 * 基础Controller
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class BaseController {

	@Autowired
	protected IBaseService baseService;
}