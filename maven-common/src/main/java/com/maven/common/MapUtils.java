package com.maven.common;

import java.util.Date;
import java.util.Map;

/**
 * Map取值类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class MapUtils {

	/**
	 * 获取Map的String类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static String getString(Map<String, Object> map, String key,
			String defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return object.toString();
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Integer类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Integer getInteger(Map<String, Object> map, String key,
			Integer defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return Integer.valueOf(object.toString());
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Long类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Long getLong(Map<String, Object> map, String key,
			Long defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return Long.valueOf(object.toString());
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Float类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Float getFloat(Map<String, Object> map, String key,
			Float defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return Float.valueOf(object.toString());
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Double类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Double getDouble(Map<String, Object> map, String key,
			Double defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return Double.valueOf(object.toString());
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Boolean类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Boolean getBoolean(Map<String, Object> map, String key,
			Boolean defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return Boolean.valueOf(object.toString());
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取Map的Date类型
	 * 
	 * @param map
	 *            集合对象
	 * @param key
	 *            Key名称
	 * @return
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
	 * @return
	 */
	public static Date getDate(Map<String, Object> map, String key,
			Date defaultValue) {
		if (StringUtils.isNotEmpty(map)) {
			Object object = map.get(key);

			if (StringUtils.isNotEmpty(object)) {
				return (Date) object;
			} else {
				return defaultValue;
			}
		} else {
			return null;
		}
	}
}