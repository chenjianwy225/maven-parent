package com.maven.common.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * Hmac加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class HMACUtils {

	private static Logger logger = LoggerFactory.getLogger(HMACUtils.class);

	// MD5加密方式
	private static final String HMACMD5 = "HmacMD5";

	// SHA1加密方式
	private static final String HMACSHA1 = "HmacSHA1";

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
				KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACMD5);
				SecretKey secretKey = keyGenerator.generateKey();
				Mac mac = Mac.getInstance(secretKey.getAlgorithm());
				mac.init(secretKey);
				byte[] bytes = mac.doFinal(source.getBytes());
				res = Hex.encodeHexString(bytes);

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
	 * @param key
	 *            秘钥
	 * @return 加密后的数据
	 */
	public static String encoderToMD5(String source, String key) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source) && StringUtils.isNotEmpty(key)) {
				SecretKey secretKey = new SecretKeySpec(key.getBytes(),
						HMACSHA1);
				Mac mac = Mac.getInstance(secretKey.getAlgorithm());
				mac.init(secretKey);
				byte[] bytes = mac.doFinal(source.getBytes());
				res = Hex.encodeHexString(bytes);

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
	 * 加密方法(SHA1)
	 * 
	 * @param source
	 *            数据源
	 * @return 加密后的数据
	 */
	public static String encoderToSHA1(String source) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
				SecretKey secretKey = keyGenerator.generateKey();
				Mac mac = Mac.getInstance(secretKey.getAlgorithm());
				mac.init(secretKey);
				byte[] bytes = mac.doFinal(source.getBytes());
				res = Hex.encodeHexString(bytes);

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
	 * 加密方法(SHA1)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            秘钥
	 * @return 加密后的数据
	 */
	public static String encoderToSHA1(String source, String key) {
		String res = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(source)) {
				SecretKey secretKey = new SecretKeySpec(key.getBytes(),
						HMACSHA1);
				Mac mac = Mac.getInstance(secretKey.getAlgorithm());
				mac.init(secretKey);
				byte[] bytes = mac.doFinal(source.getBytes());
				res = Hex.encodeHexString(bytes);

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