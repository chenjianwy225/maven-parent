package com.maven.common;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * Hmac加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class HMACUtils {

	// MD5加密方式
	private static String hmacMD5 = "HmacMD5";

	// SHA1加密方式
	private static String hmacSHA1 = "HmacSHA1";

	/**
	 * 加密方法(MD5)
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encoderToMD5(String source) {
		String res = null;

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(hmacMD5);
			SecretKey secretKey = keyGenerator.generateKey();
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			byte[] bytes = mac.doFinal(source.getBytes());
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(MD5)
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encoderToMD5(String source, String key) {
		String res = null;

		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(), hmacMD5);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			byte[] bytes = mac.doFinal(source.getBytes());
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(SHA1)
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encoderToSHA1(String source) {
		String res = null;

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(hmacSHA1);
			SecretKey secretKey = keyGenerator.generateKey();
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			byte[] bytes = mac.doFinal(source.getBytes());
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(SHA1)
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encoderToSHA1(String source, String key) {
		String res = null;

		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(), hmacSHA1);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			byte[] bytes = mac.doFinal(source.getBytes());
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
}