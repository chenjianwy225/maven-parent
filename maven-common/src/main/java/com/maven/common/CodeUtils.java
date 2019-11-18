package com.maven.common;

/**
 * 返回码枚举类
 * 
 * @author chenjian
 * @createDate 2019-01-09
 */
public enum CodeUtils {

	ERROR(-1, "服务器异常"), FAILURE(0, "请求失败"), SUCCESS(1, "请求成功"), NO_TOKEN(2,
			"Token无效或失效"), NO_PARAMETER(3, "参数错误");

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