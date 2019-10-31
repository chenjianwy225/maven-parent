package com.maven.common.request;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;
import com.maven.common.charset.CharsetUtils;

/**
 * Request请求参数类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class ParamUtils {

	private static Logger logger = LoggerFactory.getLogger(ParamUtils.class);

	/**
	 * 获取Request的String类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return String对象
	 */
	public static String getString(HttpServletRequest request, String key) {
		return getStringDefault(request, key, null);
	}

	/**
	 * 获取Request的String类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return String对象
	 */
	public static String getStringDefault(HttpServletRequest request,
			String key, String defaultValue) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? CharsetUtils
						.charsetConverter(object) : defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的Integer类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return Integer对象
	 */
	public static Integer getInteger(HttpServletRequest request, String key) {
		return getIntegerDefault(request, key, null);
	}

	/**
	 * 获取Request的Integer类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Integer对象
	 */
	public static Integer getIntegerDefault(HttpServletRequest request,
			String key, Integer defaultValue) {
		Integer result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? Integer
						.valueOf(object) : defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的Long类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return Long对象
	 */
	public static Long getLong(HttpServletRequest request, String key) {
		return getLongDefault(request, key, null);
	}

	/**
	 * 获取Request的Long类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Long对象
	 */
	public static Long getLongDefault(HttpServletRequest request, String key,
			Long defaultValue) {
		Long result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? Long.valueOf(object)
						: defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的Float类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return Float对象
	 */
	public static Float getFloat(HttpServletRequest request, String key) {
		return getFloatDefault(request, key, null);
	}

	/**
	 * 获取Request的Float类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Float对象
	 */
	public static Float getFloatDefault(HttpServletRequest request, String key,
			Float defaultValue) {
		Float result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? Float.valueOf(object)
						: defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的Double类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return Double对象
	 */
	public static Double getDouble(HttpServletRequest request, String key) {
		return getDoubleDefault(request, key, null);
	}

	/**
	 * 获取Request的Double类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Double对象
	 */
	public static Double getDoubleDefault(HttpServletRequest request,
			String key, Double defaultValue) {
		Double result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? Double
						.valueOf(object) : defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的Boolean类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return Boolean对象
	 */
	public static Boolean getBoolean(HttpServletRequest request, String key) {
		return getBooleanDefault(request, key, null);
	}

	/**
	 * 获取Request的Boolean类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return Boolean对象
	 */
	public static Boolean getBooleanDefault(HttpServletRequest request,
			String key, Boolean defaultValue) {
		Boolean result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String object = request.getParameter(key);
				result = StringUtils.isNotEmpty(object) ? Boolean
						.valueOf(object) : defaultValue;

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}

	/**
	 * 获取Request的集合类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return String集合
	 */
	public static String[] getValues(HttpServletRequest request, String key,
			String[] defaultValue) {
		return getValuesDefault(request, key, null);
	}

	/**
	 * 获取Request的集合类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @param defaultValue
	 *            默认值
	 * @return String集合
	 */
	public static String[] getValuesDefault(HttpServletRequest request,
			String key, String[] defaultValue) {
		String[] result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(request) && StringUtils.isNotEmpty(key)) {
				String[] objects = request.getParameterValues(key);

				if (StringUtils.isNotEmpty(objects)) {
					for (String string : objects) {
						string = CharsetUtils.charsetConverter(string);
					}
				} else {
					result = defaultValue;
				}

				logger.info("Get request parameter success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get request parameter error");
		}

		return result;
	}
}