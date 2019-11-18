package com.maven.common.search.entity;

import java.io.Serializable;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

/**
 * Solr排序实体类
 * 
 * @author chenjian
 * @createDate 2019-11-06
 */
public class SolrSortEntity implements Serializable {

	private static final long serialVersionUID = -8730686519058168788L;

	// 字段名
	private String fieldName;

	// 排序方式
	private ORDER sort = SolrQuery.ORDER.asc;

	/**
	 * 构造函数
	 * 
	 * @param fieldName
	 *            字段名
	 */
	public SolrSortEntity(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ORDER getSort() {
		return sort;
	}

	public void setSort(ORDER sort) {
		this.sort = sort;
	}
}