package com.maven.common.request;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * Map取值类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class MapUtils {

	private static Logger logger = LoggerFactory.getLogger(MapUtils.class);

	/**
	 * 获取Map的Object类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Object对象
	 */
	public static Object get(Map<String, Object> map, String key) {
		return get(map, key, null);
	}

	/**
	 * 获取Map的Object类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Object对象
	 */
	public static Object get(Map<String, Object> map, String key,
			Object defaultValue) {
		Object result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				result = map.get(key);

				logger.info("Get object success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get object error");
		}

		return result;
	}

	/**
	 * 获取Map的String类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return String对象
	 */
	public static String getString(Map<String, Object> map, String key) {
		return getString(map, key, null);
	}

	/**
	 * 获取Map的String类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return String对象
	 */
	public static String getString(Map<String, Object> map, String key,
			String defaultValue) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? object.toString()
						: defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Integer类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Integer对象
	 */
	public static Integer getInteger(Map<String, Object> map, String key) {
		return getInteger(map, key, null);
	}

	/**
	 * 获取Map的Integer类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Integer对象
	 */
	public static Integer getInteger(Map<String, Object> map, String key,
			Integer defaultValue) {
		Integer result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Integer
						.valueOf(object.toString()) : defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Long类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Long对象
	 */
	public static Long getLong(Map<String, Object> map, String key) {
		return getLong(map, key, null);
	}

	/**
	 * 获取Map的Long类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Long对象
	 */
	public static Long getLong(Map<String, Object> map, String key,
			Long defaultValue) {
		Long result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Long.valueOf(object
						.toString()) : defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Float类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Float对象
	 */
	public static Float getFloat(Map<String, Object> map, String key) {
		return getFloat(map, key, null);
	}

	/**
	 * 获取Map的Float类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Float对象
	 */
	public static Float getFloat(Map<String, Object> map, String key,
			Float defaultValue) {
		Float result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Float.valueOf(object
						.toString()) : defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Double类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Double对象
	 */
	public static Double getDouble(Map<String, Object> map, String key) {
		return getDouble(map, key, null);
	}

	/**
	 * 获取Map的Double类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Double对象
	 */
	public static Double getDouble(Map<String, Object> map, String key,
			Double defaultValue) {
		Double result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Double.valueOf(object
						.toString()) : defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Boolean类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Boolean对象
	 */
	public static Boolean getBoolean(Map<String, Object> map, String key) {
		return getBoolean(map, key, null);
	}

	/**
	 * 获取Map的Boolean类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Boolean对象
	 */
	public static Boolean getBoolean(Map<String, Object> map, String key,
			Boolean defaultValue) {
		Boolean result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Boolean
						.valueOf(object.toString()) : defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * 获取Map的Date类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return Date对象
	 */
	public static Date getDate(Map<String, Object> map, String key) {
		return getDate(map, key, null);
	}

	/**
	 * 获取Map的Date类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Date对象
	 */
	public static Date getDate(Map<String, Object> map, String key,
			Date defaultValue) {
		Date result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? (Date) object
						: defaultValue;

				logger.info("Get string success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}
}