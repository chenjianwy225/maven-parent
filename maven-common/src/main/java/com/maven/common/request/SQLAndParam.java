package com.maven.common.request;

import java.util.List;
import java.util.Map;

/**
 * SQL或HQL语句参数类
 * 
 * @author chenjian
 * @createDate 2019-07-22
 */
public class SQLAndParam {

	// SQL语句
	private String sql;

	// 参数集合
	private List<Object> paramList;

	// 参数集合
	private Map<String, Object> paramMap;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParamList() {
		return paramList;
	}

	public void setParamList(List<Object> paramList) {
		this.paramList = paramList;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

}