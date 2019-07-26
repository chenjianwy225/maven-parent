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
 * 文章分享实体类
 * 
 * @author chenjian
 * @createDate 2019-07-25
 */
@Entity
@Table(name = "f_article_share")
public class ArticleShare extends IBaseModel {

	private static final long serialVersionUID = 7592473818129401979L;

	public static final String tableName = "f_article_share";

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