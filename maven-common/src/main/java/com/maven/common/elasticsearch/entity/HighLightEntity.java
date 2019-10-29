package com.maven.common.elasticsearch.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 高亮实体类
 * 
 * @author chenjian
 * @createDate 2019-10-29
 */
public class HighLightEntity implements Serializable {

	private static final long serialVersionUID = 2205458421659484829L;

	// 字段集合
	private List<String> fields = new ArrayList<String>();

	// 高亮前缀
	private String preTags = "<font color='red'>";

	// 高亮后缀
	private String postTags = "</font>";

	/**
	 * 构造函数
	 * 
	 * @param fields
	 *            字段集合
	 */
	public HighLightEntity(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public String getPreTags() {
		return preTags;
	}

	public void setPreTags(String preTags) {
		this.preTags = preTags;
	}

	public String getPostTags() {
		return postTags;
	}

	public void setPostTags(String postTags) {
		this.postTags = postTags;
	}
}