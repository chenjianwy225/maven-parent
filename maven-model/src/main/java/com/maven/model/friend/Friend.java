package com.maven.model.friend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.user.User;

/**
 * 好友信息
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Entity
@Table(name = "f_friend")
public class Friend implements Serializable {

	private static final long serialVersionUID = 5770883009339614082L;

	public static final String tableName = "f_friend";

	/**
	 * 编号(主键)
	 */
	@Id
	@Column(unique = true, nullable = false)
	private String keyId;

	/**
	 * 创建日期
	 */
	@Column(name = "createDate", nullable = false)
	private Date createDate = new Date();

	/**
	 * 用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "keyId")
	private User mainUser;

	/**
	 * 好友对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "friendId", referencedColumnName = "keyId")
	private User friendUser;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getMainUser() {
		return mainUser;
	}

	public void setMainUser(User mainUser) {
		this.mainUser = mainUser;
	}

	public User getFriendUser() {
		return friendUser;
	}

	public void setFriendUser(User friendUser) {
		this.friendUser = friendUser;
	}

	public static String getTablename() {
		return tableName;
	}
}