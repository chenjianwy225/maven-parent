package com.maven.common.crypto;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class Base64Utils {

	/**
	 * 加密方法
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encoder(String source) {
		Base64 base64 = new Base64();
		return new String(base64.encode(source.getBytes()));
	}

	/**
	 * 解密方法
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String decoder(String source) {
		Base64 base64 = new Base64();
		return new String(base64.decode(source));
	}
}