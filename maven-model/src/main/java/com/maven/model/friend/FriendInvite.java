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
 * 好友邀请信息
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Entity
@Table(name = "f_friend_invite")
public class FriendInvite implements Serializable {

	private static final long serialVersionUID = 7234074155562159261L;

	public static final String tableName = "f_friend_invite";

	/**
	 * 编号(主键)
	 */
	@Id
	@Column(unique = true, nullable = false)
	private String keyId;

	/**
	 * 邀请内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 创建日期
	 */
	@Column(name = "createDate", nullable = false)
	private Date createDate = new Date();

	/**
	 * 邀请用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "sendId", referencedColumnName = "keyId")
	private User sender;

	/**
	 * 被邀请用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "acceptId", referencedColumnName = "keyId")
	private User accepter;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getAccepter() {
		return accepter;
	}

	public void setAccepter(User accepter) {
		this.accepter = accepter;
	}
}