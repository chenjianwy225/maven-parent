package com.maven.model.game;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.IBaseModel;

/**
 * 游戏分类实体类
 * 
 * @author chenjian
 * @createDate 2019-08-14
 */
@Entity
@Table(name = "f_game_type")
public class GameType extends IBaseModel {

	private static final long serialVersionUID = 6339630501230949229L;

	public static final String tableName = "f_game_type";

	/**
	 * 分类名称
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 父级编号
	 */
	@OneToOne(targetEntity = GameType.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId", referencedColumnName = "keyId")
	private GameType gameType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
}