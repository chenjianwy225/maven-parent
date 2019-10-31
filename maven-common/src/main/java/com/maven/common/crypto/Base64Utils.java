package com.maven.common.crypto;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * Base64加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class Base64Utils {

	private static Logger logger = LoggerFactory.getLogger(Base64Utils.class);

	/**
	 * 加密方法
	 * 
	 * @param source
	 *            数据源
	 * @return 加密后的数据
	 */
	public static String encoder(String source) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				Base64 base64 = new Base64();
				result = new String(base64.encode(source.getBytes()));

				logger.info("Encoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
		}

		return result;
	}

	/**
	 * 解密方法
	 * 
	 * @param source
	 *            数据源
	 * @return 解密后的原始数据
	 */
	public static String decoder(String source) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				Base64 base64 = new Base64();
				result = new String(base64.decode(source));

				logger.info("Decoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Decoder error");
		}

		return result;
	}
}