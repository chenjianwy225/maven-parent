package com.maven.common.charset;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 字符集类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class CharsetUtils {

	private static Logger logger = LoggerFactory.getLogger(CharsetUtils.class);

	// ISO编码
	public static final String CHARSET_ISO = "ISO-8859-1";

	// GB2312编码
	public static final String CHARSET_GB2312 = "GB2312";

	// GBK编码
	public static final String CHARSET_GBK = "GBK";

	// UTF-8编码
	public static final String CHARSET_UTF8 = "UTF-8";

	// BIG5编码
	public static final String CHARSET_BIG5 = "big5";

	/**
	 * 获取String的字符集
	 * 
	 * @param str
	 *            数据源
	 * @return 数据源的字符集
	 */
	public static String getCharset(String str) {
		String result = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				if (str.equals(new String(str.getBytes(CHARSET_ISO),
						CHARSET_ISO))) {
					result = CHARSET_ISO;
				} else if (str.equals(new String(str.getBytes(CHARSET_GB2312),
						CHARSET_GB2312))) {
					result = CHARSET_GB2312;
				} else if (str.equals(new String(str.getBytes(CHARSET_GBK),
						CHARSET_GBK))) {
					result = CHARSET_GBK;
				} else if (str.equals(new String(str.getBytes(CHARSET_UTF8),
						CHARSET_UTF8))) {
					result = CHARSET_UTF8;
				} else if (str.equals(new String(str.getBytes(CHARSET_BIG5),
						CHARSET_BIG5))) {
					result = CHARSET_BIG5;
				}

				message = "Get charset success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Get charset error");
		}

		return result;
	}

	/**
	 * 转换String类型的字符集
	 * 
	 * @param str
	 *            数据源
	 * @return 字符集转换后的数据
	 */
	public static String charsetConverter(String str) {
		String result = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				if (getCharset(str).equals(CHARSET_ISO)) {
					result = new String(str.getBytes(CHARSET_ISO), CHARSET_UTF8);
				} else {
					result = str;
				}

				message = "Change charset success";
			}

			logger.info(message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.info("Change charset error");
		}

		return result;
	}
}