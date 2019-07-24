package com.maven.model.article;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.IBaseModel;
import com.maven.model.user.User;

/**
 * 文章评论实体类
 * 
 * @author chenjian
 * @createDate 2019-07-24
 */
@Entity
@Table(name = "f_article_comment")
public class ArticleComment extends IBaseModel {

	private static final long serialVersionUID = -2871223266300737228L;

	public static final String tableName = "f_article_comment";

	/**
	 * 父级编号
	 */
	private String parentId;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 图片路径(以逗号','分隔)
	 */
	private String picPath;

	/**
	 * 用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "keyId")
	private User user;

	/**
	 * 文章对象
	 */
	@OneToOne(targetEntity = Article.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "articleId", referencedColumnName = "keyId")
	private Article article;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
}