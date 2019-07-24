package com.maven.model.article;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	@OneToOne(targetEntity = ArticleType.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId", referencedColumnName = "keyId")
	private ArticleType parentType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArticleType getParentType() {
		return parentType;
	}

	public void setParentType(ArticleType parentType) {
		this.parentType = parentType;
	}
}