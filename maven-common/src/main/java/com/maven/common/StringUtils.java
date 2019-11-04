package com.maven.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 字符串验证类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class StringUtils {

	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 判断Object是否为空
	 * 
	 * @param object
	 *            验证对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object object) {
		boolean result = false;

		try {
			if (object == null) {
				result = true;
			} else if (object.toString().trim().equalsIgnoreCase("")) {
				result = true;
			} else if (object.toString().trim().toLowerCase()
					.equalsIgnoreCase("null")) {
				result = true;
			}

			logger.info("Judge success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Judge error");
		}

		return result;
	}

	/**
	 * 判断Object是否不为空
	 * 
	 * @param object
	 *            验证对象
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	/**
	 * 判断Object是否JSON格式
	 * 
	 * @param object
	 *            验证对象
	 * @return 是否JSON格式
	 */
	public static boolean isJson(Object object) {
		boolean result = false;

		try {
			JSONObject.parseObject(object.toString());
			result = true;

			logger.info("Judge success");
		} catch (Exception e) {
			try {
				JSONObject.parseArray(object.toString());
				result = true;

				logger.info("Judge success");
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Judge error");
			}
		}

		return result;
	}
}