package com.maven.model.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.maven.model.IBaseModel;

/**
 * 文章分类实体类
 * 
 * @author chenjian
 * @createDate 2019-07-23
 */
@Entity
@Table(name = "f_article_type")
public class ArticleType extends IBaseModel {

	private static final long serialVersionUID = 2619862854548121925L;

	public static final String tableName = "f_article_type";

	/**
	 * 分类名称
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 父级编号
	 */
	@Column(name = "parentId")
	private String parentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}