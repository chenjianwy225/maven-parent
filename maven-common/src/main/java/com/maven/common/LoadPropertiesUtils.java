package com.maven.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取配置文件类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public class LoadPropertiesUtils {

	// 默认配置文件
	private static String DEFAULT_FILE = "demo.properties";

	private static LoadPropertiesUtils loadPropertiesUtils = null;

	private static Properties properties = null;

	/**
	 * 解析Properties文件
	 * 
	 * @return
	 */
	public static LoadPropertiesUtils getInstance() {
		return getInstance(DEFAULT_FILE);
	}

	/**
	 * 解析Properties文件
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static LoadPropertiesUtils getInstance(String path) {
		if (loadPropertiesUtils == null) {
			loadPropertiesUtils = new LoadPropertiesUtils();
		}

		try {
			properties = new Properties();
			ClassLoader classLoader = LoadPropertiesUtils.class
					.getClassLoader();
			InputStream is = classLoader.getResourceAsStream(path);
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return loadPropertiesUtils;
	}

	/**
	 * 获取文件Key对应的值
	 * 
	 * @param key
	 *            Key名称
	 * @return
	 */
	public String getKey(String key) {
		String value = null;
		try {
			value = properties.getProperty(key);
			value = new String(properties.getProperty(key).getBytes(
					"ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}