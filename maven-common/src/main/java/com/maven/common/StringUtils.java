package com.maven.common;

/**
 * 字符串验证类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class StringUtils {

	/**
	 * 判断Object是否为空
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		boolean res = false;

		if (object == null) {
			res = true;
		} else if (object.toString().trim().equalsIgnoreCase("")) {
			res = true;
		} else if (object.toString().trim().toLowerCase()
				.equalsIgnoreCase("null")) {
			res = true;
		}

		return res;
	}

	/**
	 * 判断Object是否不为空
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}
}