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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * RSA加密类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class RSAUtils {

	private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);

	// RSA加密方式
	private static final String RSA = "RSA";

	// 字符编码
	private static final String ENCODING = "UTF-8";

	/**
	 * 公钥加密,私钥解密(加密)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            公钥
	 * @return 加密后的数据
	 */
	public static String encoderToPublic(String source, String key) {
		String res = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source) && StringUtils.isNotEmpty(key)) {
				X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
						Hex.decodeHex(key));
				KeyFactory keyFactory = KeyFactory.getInstance(RSA);
				PublicKey publicKey = keyFactory
						.generatePublic(x509EncodedKeySpec);

				Cipher cipher = Cipher.getInstance(RSA);
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
				res = Hex.encodeHexString(bytes);

				logger.info("Encoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
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
	 * @return 解密后的原始数据
	 */
	public static String decoderToPublic(String source, String key) {
		String res = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source) && StringUtils.isNotEmpty(key)) {
				PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
						Hex.decodeHex(key));
				KeyFactory keyFactory = KeyFactory.getInstance(RSA);
				PrivateKey privateKey = keyFactory
						.generatePrivate(pkcs8EncodedKeySpec);

				Cipher cipher = Cipher.getInstance(RSA);
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
				res = new String(bytes, ENCODING);

				logger.info("Decoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Decoder error");
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
	 * @return 加密后的数据
	 */
	public static String encoderToPrivate(String source, String key) {
		String res = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source) && StringUtils.isNotEmpty(key)) {
				PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
						Hex.decodeHex(key));
				KeyFactory keyFactory = KeyFactory.getInstance(RSA);
				PrivateKey privateKey = keyFactory
						.generatePrivate(pkcs8EncodedKeySpec);

				Cipher cipher = Cipher.getInstance(RSA);
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
				byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
				res = Hex.encodeHexString(bytes);

				logger.info("Encoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encoder error");
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
	 * @return 解密后的原始数据
	 */
	public static String decoderToPrivate(String source, String key) {
		String res = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(source) && StringUtils.isNotEmpty(key)) {
				X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
						Hex.decodeHex(key));
				KeyFactory keyFactory = KeyFactory.getInstance(RSA);
				PublicKey publicKey = keyFactory
						.generatePublic(x509EncodedKeySpec);

				Cipher cipher = Cipher.getInstance(RSA);
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
				byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
				res = new String(bytes, ENCODING);

				logger.info("Decoder success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Decoder error");
		}

		return res;
	}

	/**
	 * 获取随机秘钥
	 * 
	 * @return 公钥和私钥Map
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

			logger.info("Get secretKey success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Get secretKey error");
		}

		return map;
	}
}