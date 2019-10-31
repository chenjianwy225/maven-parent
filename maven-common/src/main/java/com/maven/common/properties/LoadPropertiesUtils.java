package com.maven.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 获取配置文件类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public class LoadPropertiesUtils {

	private static Logger logger = LoggerFactory
			.getLogger(LoadPropertiesUtils.class);

	// 默认配置文件
	private static String DEFAULT_FILE = "demo.properties";

	// LoadPropertiesUtils对象
	private static LoadPropertiesUtils loadPropertiesUtils = null;

	// Properties对象
	private static Properties properties = null;

	/**
	 * 解析Properties文件
	 * 
	 * @return LoadPropertiesUtils对象
	 */
	public static LoadPropertiesUtils getInstance() {
		return getInstance(DEFAULT_FILE);
	}

	/**
	 * 解析Properties文件
	 * 
	 * @param path
	 *            文件路径
	 * @return LoadPropertiesUtils对象
	 */
	public static LoadPropertiesUtils getInstance(String path) {
		if (loadPropertiesUtils == null) {
			loadPropertiesUtils = new LoadPropertiesUtils();
		}

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(path)) {
				properties = new Properties();
				ClassLoader classLoader = LoadPropertiesUtils.class
						.getClassLoader();
				InputStream is = classLoader.getResourceAsStream(path);
				properties.load(is);

				logger.info("Init success");
			} else {
				logger.info("Parameter error");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Init error");
		}

		return loadPropertiesUtils;
	}

	/**
	 * 获取文件Key对应的值
	 * 
	 * @param key
	 *            Key名称
	 * @return Key值
	 */
	public String getKey(String key) {
		String value = null;
		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(key)) {
				value = properties.getProperty(key);
				value = new String(properties.getProperty(key).getBytes(
						"ISO-8859-1"), "UTF-8");

				logger.info("Get key success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get key error");
		}
		return value;
	}
}