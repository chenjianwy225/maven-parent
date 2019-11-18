package com.maven.common.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.maven.common.StringUtils;
import com.maven.common.request.MapUtils;

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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.parse(str);

				message = "Change date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change date error");
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(str)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.parse(str).getTime();

				message = "Change date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change date error");
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(date)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);

				message = "Change date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change date error");
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
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(lon)) {
				format = StringUtils.isEmpty(format) ? YMDHMS : format;

				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(new Date(lon));

				message = "Change date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Change date error");
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
			String message = "Parameter error";

			// 判断第一个日期
			if (StringUtils.isNotEmpty(dateOne)) {
				// 判断时间对象类型
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
				// 判断时间对象类型
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

				message = "Compare date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Compare date error");
		}

		return result;
	}

	/**
	 * 计算时间与当前时间差值(JSONObject)
	 * 
	 * @param date
	 *            时间对象
	 * @return 时间差值
	 */
	public static JSONObject calculateDateToMap(Object date) {
		JSONObject result = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(date)) {
				long time = 0l;

				// 判断时间对象类型
				if (date instanceof Date) {
					time = ((Date) date).getTime();
				} else if (date instanceof String) {
					time = strToLong(date.toString());
				} else if (date instanceof Long) {
					time = (Long) date;
				} else if (date instanceof Integer) {
					time = Integer.valueOf(date.toString()).longValue();
				}

				result = calculateDate(time);
				message = "Get calculate date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get calculate date error");
		}

		return result;
	}

	/**
	 * 计算时间与当前时间差值(String)
	 * 
	 * @param date
	 *            时间对象
	 * @return 时间差值
	 */
	public static String calculateDateToStr(Object date) {
		String result = null;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(date)) {
				long time = 0l;

				// 判断时间对象类型
				if (date instanceof Date) {
					time = ((Date) date).getTime();
				} else if (date instanceof String) {
					time = strToLong(date.toString());
				} else if (date instanceof Long) {
					time = (Long) date;
				} else if (date instanceof Integer) {
					time = Integer.valueOf(date.toString()).longValue();
				}

				JSONObject jsonObject = calculateDate(time);
				Integer years = MapUtils.getInteger(jsonObject, "years", 0)
						.intValue();
				Integer months = MapUtils.getInteger(jsonObject, "months", 0)
						.intValue();
				Integer days = MapUtils.getInteger(jsonObject, "days", 0)
						.intValue();
				Integer hours = MapUtils.getInteger(jsonObject, "hours", 0)
						.intValue();
				Integer minutes = MapUtils.getInteger(jsonObject, "minutes", 0)
						.intValue();
				Integer seconds = MapUtils.getInteger(jsonObject, "seconds", 0)
						.intValue();

				StringBuffer buffer = new StringBuffer();
				// 判断是否有年
				if (years.intValue() > 0) {
					buffer.append(years.toString() + "年");
				}

				// 判断是否有月
				if (months.intValue() > 0) {
					buffer.append(months.toString() + "月");
				}

				// 判断是否有日
				if (days.intValue() > 0) {
					buffer.append(days.toString() + "日");
				}

				// 判断是否有时
				if (hours.intValue() > 0) {
					buffer.append(hours.toString() + "时");
				}

				// 判断是否有分
				if (minutes.intValue() > 0) {
					buffer.append(minutes.toString() + "分");
				}

				// 判断是否有秒
				if (seconds.intValue() > 0) {
					buffer.append(seconds.toString() + "秒");
				}

				result = buffer.toString();
				message = "Get calculate date success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get calculate date error");
		}

		return result;
	}

	/**
	 * 获取时间与当前时间的间隔值
	 * 
	 * @param time
	 *            时间对象
	 * @return 与当前时间的间隔值
	 */
	private static JSONObject calculateDate(long time) {
		long sourceTime = time;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);

		// 判断时间是否小于当前时间
		if (time < calendar.getTimeInMillis()) {
			sourceTime = calendar.getTimeInMillis();
			calendar.setTimeInMillis(time);
		}

		int years = spaceNum(calendar, sourceTime, Calendar.YEAR);
		int months = spaceNum(calendar, sourceTime, Calendar.MONTH);
		int days = spaceNum(calendar, sourceTime, Calendar.DATE);
		int hours = spaceNum(calendar, sourceTime, Calendar.HOUR);
		int minutes = spaceNum(calendar, sourceTime, Calendar.MINUTE);
		int seconds = spaceNum(calendar, sourceTime, Calendar.SECOND);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("years", years);
		jsonObject.put("months", months);
		jsonObject.put("days", days);
		jsonObject.put("hours", hours);
		jsonObject.put("minutes", minutes);
		jsonObject.put("seconds", seconds);

		return jsonObject;
	}

	/**
	 * 获取年、月、日、时、分、秒与当前时间的间隔值
	 * 
	 * @param calendar
	 *            起始时间
	 * @param date
	 *            结束时间
	 * @param mode
	 *            时间类型
	 * @return 与当前时间的间隔值
	 */
	private static int spaceNum(Calendar calendar, long date, int mode) {
		int result = 0;
		boolean isContinue = true;

		while (isContinue) {
			calendar.add(mode, 1);

			if (calendar.getTimeInMillis() > date) {
				isContinue = false;
			} else {
				result += 1;
			}
		}

		calendar.add(mode, -1);

		return result;
	}
}