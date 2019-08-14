package com.maven.model.game;

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

/**
 * 游戏实体类
 * 
 * @author chenjian
 * @createDate 2019-08-14
 */
@Entity
@Table(name = "f_game")
public class Game extends IBaseModel {

	private static final long serialVersionUID = -33943761269474431L;

	public static final String tableName = "f_game";

	/**
	 * 标题
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 摘要
	 */
	@Column(name = "summary", nullable = false)
	private String summary;

	/**
	 * 内容
	 */
	@Column(name = "content", nullable = false)
	private String content;

	/**
	 * 图片路径(以逗号','分隔)
	 */
	@Column(name = "picPath", nullable = false)
	private String picPath;

	/**
	 * 视频路径(以逗号','分隔)
	 */
	@Column(name = "videoPath")
	private String videoPath;

	/**
	 * 下载路径
	 */
	@Column(name = "urlPath", nullable = false)
	private String urlPath;

	/**
	 * 浏览量
	 */
	@Column(name = "hits", columnDefinition = "int default 0")
	private int hits = 0;

	/**
	 * 浏览量
	 */
	@Column(name = "downloads", columnDefinition = "int default 0")
	private int downloads = 0;

	/**
	 * 游戏分类对象
	 */
	@OneToOne(targetEntity = GameType.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "typeId", referencedColumnName = "keyId")
	private GameType gameType;

	/**
	 * 游戏评论集合
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.EAGER)
	private List<GameComment> commentList = new ArrayList<GameComment>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public List<GameComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<GameComment> commentList) {
		this.commentList = commentList;
	}
}