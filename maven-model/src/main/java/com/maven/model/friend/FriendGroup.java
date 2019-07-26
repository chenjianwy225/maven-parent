package com.maven.model.friend;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 好友分组信息
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Entity
@Table(name = "f_friend_group")
public class FriendGroup implements Serializable {

	private static final long serialVersionUID = -2472436535349677359L;

	public static final String tableName = "f_friend_group";

	/**
	 * 编号(主键)
	 */
	@Id
	@Column(unique = true, nullable = false)
	private String keyId;

	/**
	 * 好友对象
	 */
	@OneToOne(targetEntity = Friend.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "friendId", referencedColumnName = "keyId")
	private Friend friend;

	/**
	 * 分组对象
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "groupId")
	private Group group;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Friend getFriend() {
		return friend;
	}

	public void setFriend(Friend friend) {
		this.friend = friend;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}