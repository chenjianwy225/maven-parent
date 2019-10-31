package com.maven.common;

/**
 * 返回码枚举类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public enum CodeUtils {
	SUCCESS(0, "请求成功"), ERROR(-1, "请求失败");

	// 返回编码
	private final int code;

	// 返回信息
	private final String message;

	/**
	 * 构造函数
	 * 
	 * @param code
	 *            编码
	 * @param message
	 *            信息
	 */
	private CodeUtils(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}