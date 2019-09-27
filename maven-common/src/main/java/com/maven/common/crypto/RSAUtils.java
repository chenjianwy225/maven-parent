package com.maven.common.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

/**
 * RSA加密类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class RSAUtils {

	// 字符编码
	private static final String ENCODING = "UTF-8";

	// RSA加密方式
	private static final String RSA = "RSA";

	/**
	 * 公钥加密,私钥解密(加密)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            公钥
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encoderToPublic(String source, String key) {
		String res = null;

		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
					Hex.decodeHex(key));
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 公钥加密,私钥解密(解密)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            私钥
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decoderToPublic(String source, String key) {
		String res = null;

		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
					Hex.decodeHex(key));
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = keyFactory
					.generatePrivate(pkcs8EncodedKeySpec);

			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
			res = new String(bytes, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 私钥加密,公钥解密(加密)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            私钥
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encoderToPrivate(String source, String key) {
		String res = null;

		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
					Hex.decodeHex(key));
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey privateKey = keyFactory
					.generatePrivate(pkcs8EncodedKeySpec);

			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 私钥加密,公钥解密(解密)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            公钥
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decoderToPrivate(String source, String key) {
		String res = null;

		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
					Hex.decodeHex(key));
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
			res = new String(bytes, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 获取随机秘钥
	 * 
	 * @return 返回公钥和私钥Map
	 * @throws Exception
	 */
	public static Map<String, Object> getKey() {
		Map<String, Object> map = null;

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator
					.getInstance(RSA);
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

			map = new HashMap<String, Object>();
			map.put("publicKey", Hex.encodeHexString(rsaPublicKey.getEncoded()));
			map.put("privateKey",
					Hex.encodeHexString(rsaPrivateKey.getEncoded()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
}