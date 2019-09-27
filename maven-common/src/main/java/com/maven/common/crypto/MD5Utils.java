package com.maven.common.crypto;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

/**
 * MD5加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class MD5Utils {

	/**
	 * 加密方法(MD2)
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encoderToMD2(String source) {
		String res = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD2");
			byte[] digest = md5.digest(source.getBytes());
			res = Hex.encodeHexString(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

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
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(source.getBytes());
			res = Hex.encodeHexString(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(SHA)
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encoderToSHA(String source) {
		String res = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			byte[] digest = md5.digest(source.getBytes());
			res = Hex.encodeHexString(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
}