package com.maven.common.request;

import javax.servlet.http.HttpServletRequest;

import com.maven.common.StringUtils;
import com.maven.common.charset.CharsetUtils;

/**
 * Request请求参数类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class ParamUtils {

	/**
	 * 获取Request的String类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static String getStringDefault(HttpServletRequest request,
			String key, String defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			value = CharsetUtils.charsetConverter(value);

			if (StringUtils.isNotEmpty(value)) {
				return value;
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的Integer类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Integer getIntegerDefault(HttpServletRequest request,
			String key, Integer defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(value)) {
				return Integer.valueOf(value);
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的Long类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Long getLongDefault(HttpServletRequest request, String key,
			Long defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(value)) {
				return Long.valueOf(value);
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的Float类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Float getFloatDefault(HttpServletRequest request, String key,
			Float defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(value)) {
				return Float.valueOf(value);
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的Double类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Double getDoubleDefault(HttpServletRequest request,
			String key, Double defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(value)) {
				return Double.valueOf(value);
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的Boolean类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Boolean getBooleanDefault(HttpServletRequest request,
			String key, Boolean defaultValue) {
		String value = request.getParameter(key);

		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(value)) {
				return Boolean.valueOf(value);
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * 获取Request的集合类型
	 * 
	 * @param request
	 *            Request请求对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static String[] getValuesDefault(HttpServletRequest request,
			String key, String[] defaultValue) {
		String[] value = request.getParameterValues(key);

		if (StringUtils.isNotEmpty(value)) {
			for (String string : value) {
				string = CharsetUtils.charsetConverter(string);
			}
		} else {
			value = defaultValue;
		}
		return defaultValue;
	}
}