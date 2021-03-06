package com.maven.model.article;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	private static final long serialVersionUID = 919153569604787730L;

	public static final String tableName = "f_article_comment";

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
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "articleId")
	private Article article;

	/**
	 * 父级对象
	 */
	@OneToOne(targetEntity = ArticleComment.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId", referencedColumnName = "keyId")
	private ArticleComment articleComment;

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

	public ArticleComment getArticleComment() {
		return articleComment;
	}

	public void setArticleComment(ArticleComment articleComment) {
		this.articleComment = articleComment;
	}
}