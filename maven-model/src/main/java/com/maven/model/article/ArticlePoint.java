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
 * 文章点赞实体类
 * 
 * @author chenjian
 * @createDate 2019-07-24
 */
@Entity
@Table(name = "f_article_point")
public class ArticlePoint extends IBaseModel {

	private static final long serialVersionUID = 2552989769767063373L;

	public static final String tableName = "f_article_point";

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