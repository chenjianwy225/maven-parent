package com.maven.common.search.entity;

import java.io.Serializable;

import org.elasticsearch.search.sort.SortOrder;

/**
 * Elastic排序实体类
 * 
 * @author chenjian
 * @createDate 2019-10-29
 */
public class ElasticSortEntity implements Serializable {

	private static final long serialVersionUID = -3070804445703149154L;

	// 字段名
	private String fieldName;

	// 排序方式
	private SortOrder sort = SortOrder.ASC;

	/**
	 * 构造函数
	 * 
	 * @param fieldName
	 *            字段名
	 */
	public ElasticSortEntity(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public SortOrder getSort() {
		return sort;
	}

	public void setSort(SortOrder sort) {
		this.sort = sort;
	}
}