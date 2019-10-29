package com.maven.common.elasticsearch.entity;

import java.io.Serializable;

/**
 * 条件实体类
 * 
 * @author chenjian
 * @createDate 2019-10-25
 */
public class QueryEntity implements Serializable {

	private static final long serialVersionUID = -7122756463416420039L;

	// 关键字
	private String keyword;

	// 性别
	private String sex;

	// 最小年龄
	private int minAge = 0;

	// 最大年龄
	private int maxAge = 0;

	// 地址
	private String address;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}