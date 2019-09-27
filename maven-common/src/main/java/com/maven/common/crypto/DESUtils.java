package com.maven.common.crypto;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * DES加密类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class DESUtils {

	// 向量
	private static final String IV = "1234567-";

	// 字符编码
	private static final String ENCODING = "UTF-8";

	// DES加密方式
	public static final String DES = "DES";

	// 3DES加密方式
	public static final String DESEDE = "DESede";

	// AES加密方式
	public static final String AES = "AES";

	// DES填充方式(ECB)
	private static final String DESPADDING_ECB = "DES/ECB/PKCS5Padding";

	// DES填充方式(CBC)
	private static final String DESPADDING_CBC = "DES/CBC/PKCS5Padding";

	// 3DES填充方式
	private static final String DESEDEPADDING = "DESede/ECB/PKCS5Padding";

	// AES填充方式
	private static final String AESPADDING = "AES/ECB/PKCS5padding";

	/**
	 * 加密方法(DES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,长度必须是8的倍数
	 * @param isIv
	 *            是否使用向量加密
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encoderToDES(String source, String key, boolean isIv) {
		String res = null;

		try {
			// 生成key
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(ENCODING));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			Cipher cipher = null;
			// 判断是否使用向量
			if (isIv) {
				// 向量
				IvParameterSpec iv = new IvParameterSpec(IV.getBytes(ENCODING));

				cipher = Cipher.getInstance(DESPADDING_CBC);
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			} else {
				cipher = Cipher.getInstance(DESPADDING_ECB);
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}
			byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));

			// --通过base64,将加密数组转换成字符串
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 解密方法(DES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,长度必须是8的倍数
	 * @param isIv
	 *            是否使用向量加密
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decoderToDES(String source, String key, boolean isIv) {
		String res = null;

		try {
			// 解密key
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(ENCODING));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			Cipher cipher = null;
			// 判断是否使用向量
			if (isIv) {
				// 向量
				IvParameterSpec iv = new IvParameterSpec(IV.getBytes(ENCODING));

				cipher = Cipher.getInstance(DESPADDING_CBC);
				cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			} else {
				cipher = Cipher.getInstance(DESPADDING_ECB);
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			}
			byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
			res = new String(bytes, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(3DES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,key必须是长度大于等于 3 * 8
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encoderToDESEDE(String source, String key) {
		String res = null;

		try {
			// 生成key
			DESedeKeySpec desKeySpec = new DESedeKeySpec(key.getBytes(ENCODING));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory
					.getInstance(DESEDE);
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

			Cipher cipher = Cipher.getInstance(DESEDEPADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 解密方法(3DES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,key必须是长度大于等于 3 * 8
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decoderToDESEDE(String source, String key) {
		String res = null;

		try {
			// 解密key
			DESedeKeySpec desKeySpec = new DESedeKeySpec(key.getBytes(ENCODING));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DESEDE);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			Cipher cipher = Cipher.getInstance(DESEDEPADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(Hex.decodeHex(source));
			res = new String(bytes, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 加密方法(AES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,key必须是长度大于等于 3 * 8
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encoderToAES(String source, String key) {
		String res = null;

		try {
			// 生成key
			Key secretKey = new SecretKeySpec(key.getBytes(), AES);

			Cipher cipher = Cipher.getInstance(AESPADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(source.getBytes(ENCODING));
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 解密方法(AES)
	 * 
	 * @param source
	 *            数据源
	 * @param key
	 *            密钥,key必须是长度大于等于 3 * 8
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decoderToAES(String source, String key) {
		String res = null;

		try {
			// 解密key
			Key secretKey = new SecretKeySpec(key.getBytes(), AES);

			Cipher cipher = Cipher.getInstance(AESPADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
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
	 * @return 返回秘钥s
	 * @throws Exception
	 */
	public static String getKey(String codeMode) {
		String res = null;

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(codeMode);
			// 指定key长度，同时也是密钥长度(56位)
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] bytes = secretKey.getEncoded();
			res = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
}