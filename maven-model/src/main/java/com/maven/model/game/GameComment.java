package com.maven.model.game;

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
 * 游戏评论实体类
 * 
 * @author chenjian
 * @createDate 2019-08-14
 */
@Entity
@Table(name = "f_game_comment")
public class GameComment extends IBaseModel {

	private static final long serialVersionUID = -8853902843907351993L;

	public static final String tableName = "f_game_comment";

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
	 * 游戏对象
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "gameId")
	private Game game;

	/**
	 * 父级对象
	 */
	@OneToOne(targetEntity = GameComment.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId", referencedColumnName = "keyId")
	private GameComment gameComment;

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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public GameComment getGameComment() {
		return gameComment;
	}

	public void setGameComment(GameComment gameComment) {
		this.gameComment = gameComment;
	}
}