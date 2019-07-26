package com.maven.model.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.user.User;

/**
 * 分组信息
 * 
 * @author chenjian
 * @createDate 2019-07-26
 */
@Entity
@Table(name = "f_group")
public class Group implements Serializable {

	private static final long serialVersionUID = -7042970568315225996L;

	public static final String tableName = "f_group";

	/**
	 * 编号(主键)
	 */
	@Id
	@Column(unique = true, nullable = false)
	private String keyId;

	/**
	 * 分组名称
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * 创建日期
	 */
	@Column(name = "createDate", nullable = false)
	private Date createDate = new Date();

	/**
	 * 更新日期
	 */
	@Column(name = "modifyDate")
	private Date modifyDate;

	/**
	 * 用户对象
	 */
	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "keyId")
	private User user;

	/**
	 * 好友分组集合
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.EAGER)
	private List<FriendGroup> friendGroupList = new ArrayList<FriendGroup>();

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<FriendGroup> getFriendGroupList() {
		return friendGroupList;
	}

	public void setFriendGroupList(List<FriendGroup> friendGroupList) {
		this.friendGroupList = friendGroupList;
	}
}