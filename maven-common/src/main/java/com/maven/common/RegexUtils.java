package com.maven.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证类
 * 
 * @author chenjian
 * @createDate 2019-01-02
 */
public class RegexUtils {

	// 邮箱正则表达式
	private static final String REGEX_EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	// IP地址正则表达式
	private static final String REGEX_IP = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";

	// Url正则表达式
	private static final String REGEX_URL = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";

	// 邮政编码正则表达式
	private static final String REGEX_POSTALCODE = "^\\d{6}$";

	// 手机正则表达式
	private static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

	// 座机正则表达式
	private static final String REGEX_TELEPHONE = "^(\\d{3,4}-)?\\d{6,8}$";

	// 身份证正则表达式(15位)
	private static final String REGEX_IDCARD_FIFTEEN = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	// 身份证正则表达式(18位)
	private static final String REGEX_IDCARD_EIGHTEEN = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

	// 数字正则表达式
	private static final String REGEX_NUMBER = "^[0-9]+(.?[0-9]+)?$";

	// 整数正则表达式
	private static final String REGEX_INTEGER = "^-?\\d+$";

	// 正整数正则表达式
	private static final String REGEX_POSITIVE_INTEGER = "^\\d*[1-9]\\d*$";

	// 负整数正则表达式
	private static final String REGEX_NEGATIVE_INTEGER = "^-\\d*[1-9]\\d*$";

	// 非正整数正则表达式
	private static final String REGEX_NOT_POSITIVE_INTEGER = "^((-\\d+)|(0+))$";

	// 非负整数正则表达式
	private static final String REGEX_NOT_NEGATIVE_INTEGER = "^\\d+$";

	// 浮点数正则表达式
	private static final String REGEX_FLOAT = "^(-?\\d+)(\\.\\d+)?$";

	// 正浮点数正则表达式
	private static final String REGEX_POSITIVE_FLOAT = "^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";

	// 负浮点数正则表达式
	private static final String REGEX_NEGATIVE_FLOAT = "^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";

	// 非正浮点数正则表达式
	private static final String REGEX_NOT_POSITIVE_FLOAT = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";

	// 非负浮点数正则表达式
	private static final String REGEX_NOT_NEGATIVE_FLOAT = "^\\d+(\\.\\d+)?$";

	// 日期正则表达式(YMD格式)
	private static final String REGEX_DATE_YMD = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";

	// 日期正则表达式(YMDHMS格式)
	private static final String REGEX_DATE_YMDHMS = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

	// 车牌正则表达式
	private static final String REGEX_PLATENUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";

	// 身份证尾位验证集合
	private static final String[] ValCodeArr = { "1", "0", "x", "9", "8", "7",
			"6", "5", "4", "3", "2" };

	// 身份证固定集合
	private static final String[] Wi = { "7", "9", "10", "5", "8", "4", "2",
			"1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };

	// 整数类型
	public static final int INTEGER = 1;

	// 正整数类型
	public static final int POSITIVE_INTEGER = 2;

	// 负整数类型
	public static final int NEGATIVE_INTEGER = 3;

	// 非正整数类型
	public static final int NOT_POSITIVE_INTEGER = 4;

	// 非负整数类型
	public static final int NOT_NEGATIVE_INTEGER = 5;

	// 浮点数类型
	public static final int FLOAT = 1;

	// 正浮点数类型
	public static final int POSITIVE_FLOAT = 2;

	// 负浮点数类型
	public static final int NEGATIVE_FLOAT = 3;

	// 非正浮点数类型
	public static final int NOT_POSITIVE_FLOAT = 4;

	// 非负浮点数类型
	public static final int NOT_NEGATIVE_FLOAT = 5;

	/**
	 * 验证邮箱
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isEmail(String source) {
		return match(source, REGEX_EMAIL);
	}

	/**
	 * 验证IP
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isIP(String source) {
		return match(source, REGEX_IP);
	}

	/**
	 * 验证Url
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isUrl(String source) {
		return match(source, REGEX_URL);
	}

	/**
	 * 验证邮政编码
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isPostalCode(String source) {
		return match(source, REGEX_POSTALCODE);
	}

	/**
	 * 验证手机
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isMobile(String source) {
		return match(source, REGEX_MOBILE);
	}

	/**
	 * 验证座机
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isTelephone(String source) {
		return match(source, REGEX_TELEPHONE);
	}

	/**
	 * 验证身份证
	 * 
	 * @param source
	 *            待验证的字符串
	 * @param isDetail
	 *            是否详细验证
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isIDCard(String source, boolean isDetail) {
		boolean res = false;
		int lens = source.length();

		try {
			// 判断身份证长度
			if (lens == 15 || lens == 18) {
				// 判断是否详细验证
				if (isDetail) {
					String Ai = "";

					if (lens == 18) {
						Ai = source.substring(0, 17);
					} else if (lens == 15) {
						Ai = source.substring(0, 6) + "19"
								+ source.substring(6, 15);
					}

					// 判断是否是否数字
					if (isNumber(Ai)) {
						String year = Ai.substring(6, 10);// 年份
						String month = Ai.substring(10, 12);// 月份
						String day = Ai.substring(12, 14);// 月份

						// 判断日期是否有效
						if (isDate(year + "-" + month + "-" + day, false)) {
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat s = new SimpleDateFormat(
									"yyyy-MM-dd");

							// 判断年份
							if ((calendar.get(Calendar.YEAR) - Integer
									.parseInt(year)) < 150
									&& Integer.parseInt(month) > 0
									&& Integer.parseInt(month) <= 12
									&& Integer.parseInt(day) > 0
									&& Integer.parseInt(day) <= 31
									&& (calendar.getTime().getTime() - s.parse(
											year + "-" + month + "-" + day)
											.getTime()) > 0) {
								Hashtable<?, ?> h = GetAreaCode();

								// 判断省级编号是否存在
								if (h.containsKey(Ai.substring(0, 2))) {
									if (lens == 18) {
										int TotalmulAiWi = 0;
										for (int i = 0; i < 17; i++) {
											TotalmulAiWi += +Integer
													.parseInt(String.valueOf(Ai
															.charAt(i)))
													* Integer.parseInt(Wi[i]);
										}
										int modValue = TotalmulAiWi % 11;

										if (ValCodeArr[modValue]
												.equalsIgnoreCase(source
														.substring(17))) {
											res = true;
										}
									}
								}
							}
						}
					}
				} else {
					if (lens == 15) {
						res = match(source, REGEX_IDCARD_FIFTEEN);
					} else if (lens == 18) {
						res = match(source, REGEX_IDCARD_EIGHTEEN);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 验证数字
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isNumber(String source) {
		return match(source, REGEX_NUMBER);
	}

	/**
	 * 验证整数
	 * 
	 * @param source
	 *            待验证的字符串
	 * @param type
	 *            (1:整数;2:正整数;3:负整数;4:非正整数;5:非负整数 )
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isInteger(String source, int type) {
		boolean res = false;

		switch (type) {
		case 1:
			res = match(source, REGEX_INTEGER);
			break;
		case 2:
			res = match(source, REGEX_POSITIVE_INTEGER);
			break;
		case 3:
			res = match(source, REGEX_NEGATIVE_INTEGER);
			break;
		case 4:
			res = match(source, REGEX_NOT_POSITIVE_INTEGER);
			break;
		default:
			res = match(source, REGEX_NOT_NEGATIVE_INTEGER);
			break;
		}

		return res;
	}

	/**
	 * 验证浮点数
	 * 
	 * @param source
	 *            待验证的字符串
	 * @param type
	 *            (1:浮点数;2:正浮点数;3:负浮点数;4:非正浮点数;5:非负浮点数 )
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isFloat(String source, int type) {
		boolean res = false;

		switch (type) {
		case 1:
			res = match(source, REGEX_FLOAT);
			break;
		case 2:
			res = match(source, REGEX_POSITIVE_FLOAT);
			break;
		case 3:
			res = match(source, REGEX_NEGATIVE_FLOAT);
			break;
		case 4:
			res = match(source, REGEX_NOT_POSITIVE_FLOAT);
			break;
		default:
			res = match(source, REGEX_NOT_NEGATIVE_FLOAT);
			break;
		}

		return res;
	}

	/**
	 * 验证日期
	 * 
	 * @param source
	 *            待验证的字符串
	 * @param 是否包含小时分秒
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isDate(String source, boolean isDatail) {
		if (isDatail) {
			return match(source, REGEX_DATE_YMDHMS);
		} else {
			return match(source, REGEX_DATE_YMD);
		}
	}

	/**
	 * 验证车牌
	 * 
	 * @param source
	 *            待验证的字符串
	 * @return 如果source符合正则表达式格式,返回true;否则返回 false
	 */
	public static boolean isPlateNumber(String source) {
		return match(source, REGEX_PLATENUMBER);
	}

	/**
	 * 验证Url
	 * 
	 * @param source
	 *            待验证的字符串
	 * @param regex
	 *            正则表达式字符串
	 * @return 如果source符合regex的正则表达式格式,返回true;否则返回false
	 */
	public static boolean match(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	}

	/**
	 * 设置地区编码
	 * 
	 * @return
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}
}