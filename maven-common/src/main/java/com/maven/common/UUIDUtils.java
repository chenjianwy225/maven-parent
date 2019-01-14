package com.maven.common;

import java.util.UUID;

/**
 * UUID生成类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
public class UUIDUtils {

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
}