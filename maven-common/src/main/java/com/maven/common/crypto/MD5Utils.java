package com.maven.common.crypto;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * MD5加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class MD5Utils {

	private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

	// MD2加密方式
	private static final String MD2 = "MD2";

	// MD5加密方式
	private static final String MD5 = "MD5";

	// SHA加密方式
	private static final String SHA = "SHA";

	/**
	 * 加密方法(MD2)
	 * 
	 * @param source
	 *            数据源
	 * @return 加密后的数据
	 */
	public static String encoderToMD2(String source) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				MessageDigest md5 = MessageDigest.getInstance(MD2);
				byte[] digest = md5.digest(source.getBytes());
				res = Hex.encodeHexString(digest);

				message = "Encoder success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
		}

		return res;
	}

	/**
	 * 加密方法(MD5)
	 * 
	 * @param source
	 *            数据源
	 * @return 加密后的数据
	 */
	public static String encoderToMD5(String source) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				MessageDigest md5 = MessageDigest.getInstance(MD5);
				byte[] digest = md5.digest(source.getBytes());
				res = Hex.encodeHexString(digest);

				message = "Encoder success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
		}

		return res;
	}

	/**
	 * 加密方法(SHA)
	 * 
	 * @param source
	 *            数据源
	 * @return 加密后的数据
	 */
	public static String encoderToSHA(String source) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				MessageDigest md5 = MessageDigest.getInstance(SHA);
				byte[] digest = md5.digest(source.getBytes());
				res = Hex.encodeHexString(digest);

				message = "Encoder success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
		}

		return res;
	}
}