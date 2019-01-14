package com.maven.common;

import java.io.UnsupportedEncodingException;

/**
 * 字符集类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class CharsetUtils {

	public static final String CHARSET_ISO = "ISO-8859-1";
	public static final String CHARSET_GB2312 = "GB2312";
	public static final String CHARSET_GBK = "GBK";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_BIG5 = "big5";

	/**
	 * 获取String的字符集
	 * 
	 * @param str
	 *            数据源
	 * @return
	 */
	public static String getCharset(String str) {
		String res = null;

		try {
			if (StringUtils.isNotEmpty(str)) {
				if (str.equals(new String(str.getBytes(CHARSET_ISO),
						CHARSET_ISO))) {
					res = CHARSET_ISO;
				} else if (str.equals(new String(str.getBytes(CHARSET_GB2312),
						CHARSET_GB2312))) {
					res = CHARSET_GB2312;
				} else if (str.equals(new String(str.getBytes(CHARSET_GBK),
						CHARSET_GBK))) {
					res = CHARSET_GBK;
				} else if (str.equals(new String(str.getBytes(CHARSET_UTF8),
						CHARSET_UTF8))) {
					res = CHARSET_UTF8;
				} else if (str.equals(new String(str.getBytes(CHARSET_BIG5),
						CHARSET_BIG5))) {
					res = CHARSET_BIG5;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 转换String类型的字符集
	 * 
	 * @param str
	 *            数据源
	 * @return
	 */
	public static String charsetConverter(String str) {
		String res = null;

		try {
			if (StringUtils.isNotEmpty(str)) {
				if (getCharset(str).equals(CHARSET_ISO)) {
					res = new String(str.getBytes(CHARSET_ISO), CHARSET_UTF8);
				} else {
					res = str;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}
}