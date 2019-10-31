package com.maven.common;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UUID生成类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class UUIDUtils {

	private static Logger logger = LoggerFactory.getLogger(UUIDUtils.class);

	/**
	 * 获取UUID编号
	 * 
	 * @return UUID编号
	 */
	public static String getUUID() {
		String uuid = null;

		try {
			uuid = UUID.randomUUID().toString().replaceAll("-", "");

			logger.info("Create success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create error");
		}

		return uuid;
	}
}