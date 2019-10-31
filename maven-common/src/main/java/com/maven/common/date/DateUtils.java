package com.maven.common.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maven.common.StringUtils;

/**
 * 日期转换类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class DateUtils {

	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	// 日期格式(年-月-日)
	public static final String YMD = "yyyy-MM-dd";

	// 日期格式(小时:分:秒)
	public static final String HMS = "HH:mm:ss";

	// 日期格式(年-月-日 小时:分:秒)
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当前日期Date类型
	 * 
	 * @return 日期Date类型数据
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 获取当前日期Long类型
	 * 
	 * @return 日期Long类型数据
	 */
	public static Long getDateToLong() {
		return getDate().getTime();
	}

	/**
	 * 获取当前日期String类型
	 * 
	 * @return 日期String类型
	 */
	public static String getDateToStr() {
		return dateToStr(getDate(), YMDHMS);
	}

	/**
	 * 获取当前日期String类型
	 * 
	 * @param format
	 *            日期格式
	 * @return 日期String类型
	 */
	public static String getDateToStr(String format) {
		return dateToStr(getDate(), format);
	}

	/**
	 * String类型转Date类型
	 * 
	 * @param str
	 *            String类型转Date类型
	 * @return 日期Date类型
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
	 * @return 日期Date类型
	 */
	public static Date strToDate(String str, String format) {
		Date result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.parse(str);

				logger.info("Change success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return result;
	}

	/**
	 * String类型转Long类型
	 * 
	 * @param str
	 *            日期的String类型
	 * @return 日期Long类型
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
	 * @return 日期Long类型
	 */
	public static Long strToLong(String str, String format) {
		Long result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.parse(str).getTime();

				logger.info("Change success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return result;
	}

	/**
	 * Date类型转String类型
	 * 
	 * @param date
	 *            日期的Date类型
	 * @return 日期String类型
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
	 * @return 日期String类型
	 */
	public static String dateToStr(Date date, String format) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(date)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);

				logger.info("Change success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return result;
	}

	/**
	 * Long类型转String类型
	 * 
	 * @param lon
	 *            日期的Long类型
	 * @return 日期String类型
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
	 * @return 日期String类型
	 */
	public static String longToStr(Long lon, String format) {
		String result = null;

		try {
			// 判断传入参数
			if (StringUtils.isNotEmpty(lon)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(new Date(lon));

				logger.info("Change success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change error");
		}

		return result;
	}

	/**
	 * 比较两个日期的大小关系
	 * 
	 * @param dateOne
	 *            日期1
	 * @param dateTwo
	 *            日期2
	 * @return 比较值(-1:异常,0:等于,1:大于,2:小于)
	 */
	public static int compareDate(Object dateOne, Object dateTwo) {
		int result = -1;
		Long date_one = null, date_two = null;

		try {
			// 判断第一个日期
			if (StringUtils.isNotEmpty(dateOne)) {
				if (dateOne instanceof Date) {
					date_one = ((Date) dateOne).getTime();
				} else if (dateOne instanceof String) {
					date_one = strToLong(dateOne.toString());
				} else if (dateOne instanceof Long) {
					date_one = (Long) dateOne;
				} else if (dateOne instanceof Integer) {
					date_one = Integer.valueOf(dateOne.toString()).longValue();
				}
			}

			// 判断第二个日期
			if (StringUtils.isNotEmpty(dateTwo)) {
				if (dateTwo instanceof Date) {
					date_two = ((Date) dateTwo).getTime();
				} else if (dateTwo instanceof String) {
					date_two = strToLong(dateTwo.toString());
				} else if (dateTwo instanceof Long) {
					date_two = (Long) dateTwo;
				} else if (dateTwo instanceof Integer) {
					date_two = Integer.valueOf(dateTwo.toString()).longValue();
				}
			}

			// 判断两个日期是否都有值
			if (StringUtils.isNotEmpty(date_one)
					&& StringUtils.isNotEmpty(date_two)) {
				long time_one = date_one.longValue(), time_two = date_two
						.longValue();

				if (time_one > time_two) {
					result = 1;
				} else if (time_one < time_two) {
					result = 2;
				} else {
					result = 0;
				}

				logger.info("Compare success");
			} else {
				logger.info("Parameter error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Compare error");
		}

		return result;
	}
}