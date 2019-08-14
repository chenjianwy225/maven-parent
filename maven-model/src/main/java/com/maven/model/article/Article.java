package com.maven.model.article;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.IBaseModel;
import com.maven.model.user.User;

/**
 * 文章信息实体类
 * 
 * @author chenjian
 * @createDate 2019-07-23
 */
@Entity
@Table(name = "f_article")
public class Article extends IBaseModel {

	private static final long serialVersionUID = 1940028849328964119L;

	public static final String tableName = "f_article";

	/**
	 * 标题
	 */
	@Column(name = "title", nullable = false)
	private String title;

	/**
	 * 摘要
	 */
	@Column(name = "summary", nullable = false)
	private String summary;

	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 图片路径(以逗号','分隔)
	 */
	@Column(name = "picPath")
	private String picPath;

	/**
	 * 音频路径(以逗号','分隔)
	 */
	@Column(name = "audioPath")
	private String audioPath;

	/**
	 * 视频路径(以逗号','分隔)
	 */
	@Column(name = "videoPath")
	private String videoPath;

	/**
	 * 浏览量
	 */
	@Column(name = "hits", columnDefinition = "int default 0")
	private int hits = 0;

	/**
	 * 用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "keyId")
	private User user;

	/**
	 * 文章分类对象
	 */
	@OneToOne(targetEntity = ArticleType.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "typeId", referencedColumnName = "keyId")
	private ArticleType articleType;

	/**
	 * 文章评论集合
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "article", fetch = FetchType.EAGER)
	private List<ArticleComment> commentList = new ArrayList<ArticleComment>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArticleType getArticleType() {
		return articleType;
	}

	public void setArticleType(ArticleType articleType) {
		this.articleType = articleType;
	}

	public List<ArticleComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<ArticleComment> commentList) {
		this.commentList = commentList;
	}
}