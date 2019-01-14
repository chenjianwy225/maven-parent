package com.maven.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class DateUtils {

	// 日期格式(年-月-日)
	public static final String YMD = "yyyy-MM-dd";

	// 日期格式(小时:分:秒)
	public static final String HMS = "HH:mm:ss";

	// 日期格式(年-月-日 小时:分:秒)
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前日期Date类型
	 * 
	 * @return
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 获取当前日期Long类型
	 * 
	 * @return
	 */
	public static Long getDateToLong() {
		return getDate().getTime();
	}

	/**
	 * 获取当前日期String类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getDateToStr() {
		return dateToStr(getDate(), YMDHMS);
	}

	/**
	 * 获取当前日期String类型
	 * 
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static String getDateToStr(String format) {
		return dateToStr(getDate(), format);
	}

	/**
	 * String类型转Date类型
	 * 
	 * @param str
	 *            String类型转Date类型
	 * @return
	 * @throws Exception
	 */
	public static Date strToDate(String str) {
		return strToDate(str, YMDHMS);
	}

	/**
	 * String类型转Date类型
	 * 
	 * @param str
	 *            String类型转Date类型
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static Date strToDate(String str, String format) {
		Date res = null;

		if (StringUtils.isNotEmpty(str)) {
			format = StringUtils.isEmpty(format) ? YMDHMS : format;

			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try {
				res = dateFormat.parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	/**
	 * String类型转Long类型
	 * 
	 * @param str
	 *            日期的String类型
	 * @return
	 * @throws Exception
	 */
	public static Long strToLong(String str) {
		return strToLong(str, YMDHMS);
	}

	/**
	 * String类型转Long类型
	 * 
	 * @param str
	 *            日期的String类型
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static Long strToLong(String str, String format) {
		Long res = null;

		if (StringUtils.isNotEmpty(str)) {
			format = StringUtils.isEmpty(format) ? YMDHMS : format;

			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try {
				return dateFormat.parse(str).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	/**
	 * Date类型转String类型
	 * 
	 * @param date
	 *            日期的Date类型
	 * @return
	 * @throws Exception
	 */
	public static String dateToStr(Date date) {
		return dateToStr(date, YMDHMS);
	}

	/**
	 * Date类型转String类型
	 * 
	 * @param date
	 *            日期的Date类型
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static String dateToStr(Date date, String format) {
		if (StringUtils.isNotEmpty(date)) {
			format = StringUtils.isEmpty(format) ? YMDHMS : format;

			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		} else {
			return null;
		}
	}

	/**
	 * Long类型转String类型
	 * 
	 * @param lon
	 *            日期的Long类型
	 * @return
	 * @throws Exception
	 */
	public static String longToStr(Long lon) {
		return longToStr(lon, YMDHMS);
	}

	/**
	 * Long类型转String类型
	 * 
	 * @param lon
	 *            日期的Long类型
	 * @param format
	 *            日期格式
	 * @return
	 * @throws Exception
	 */
	public static String longToStr(Long lon, String format) {
		if (StringUtils.isNotEmpty(lon)) {
			format = StringUtils.isEmpty(format) ? YMDHMS : format;

			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(new Date(lon));
		} else {
			return null;
		}
	}
}