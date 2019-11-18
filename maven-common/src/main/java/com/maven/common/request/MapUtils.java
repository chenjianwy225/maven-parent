package com.maven.common.request;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				result = map.get(key);

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? object.toString()
						: defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Integer
						.valueOf(object.toString()) : defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Long.valueOf(object
						.toString()) : defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Float.valueOf(object
						.toString()) : defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Double.valueOf(object
						.toString()) : defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? Boolean
						.valueOf(object.toString()) : defaultValue;

				message = "Get string success";
			}

			logger.info(message);
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && map.size() > 0
					&& StringUtils.isNotEmpty(key)) {
				Object object = map.get(key);

				result = StringUtils.isNotEmpty(object) ? (Date) object
						: defaultValue;

				message = "Get string success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get string error");
		}

		return result;
	}

	/**
	 * Class转Map
	 * 
	 * @param obj
	 *            Class对象
	 * @return Map对象
	 */
	public static Map<String, Object> classToMap(Object object) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(object)) {
				Class<?> clazz = object.getClass();
				Field[] fields = clazz.getDeclaredFields();

				// 遍历Class数据
				for (Field field : fields) {
					field.setAccessible(true);
					String fieldName = field.getName();
					Object value = field.get(object);

					result.put(fieldName, StringUtils.isNotEmpty(value) ? value
							: "");
				}

				message = "Change class to map success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change class to map error");
		}

		return result;
	}

	/**
	 * Map转Class
	 * 
	 * @param map
	 *            Map对象
	 * @param clazz
	 *            Class对象
	 * @return Class对象
	 */
	public static <T> T mapToClass(Map<String, Object> map, Class<T> clazz) {
		T result = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(map) && StringUtils.isNotEmpty(clazz)) {
				result = clazz.newInstance();
				Field[] fields = result.getClass().getDeclaredFields();

				// 遍历Class数据
				for (Field field : fields) {
					field.setAccessible(true);
					int mod = field.getModifiers();
					String fieldName = field.getName();

					if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
						continue;
					}

					if (map.containsKey(fieldName)) {
						field.set(result, map.get(fieldName));
					}
				}

				message = "Change map to class success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change map to class error");
		}
		return result;
	}
}