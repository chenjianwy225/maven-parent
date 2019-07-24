package com.maven.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.maven.model.IBaseModel;

/**
 * 用户基础信息实体类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@Entity
@Table(name = "f_user")
public class User extends IBaseModel {

	private static final long serialVersionUID = 5838444105798522911L;

	public static final String tableName = "f_user";

	/**
	 * 用户名
	 */
	@Column(name = "userName", nullable = false)
	private String userName;

	/**
	 * 用户密码
	 */
	@Column(name = "userPassword", nullable = false)
	private String userPassword;

	/**
	 * 手机号码
	 */
	@Column(name = "mobile", nullable = false)
	private String mobile;

	/**
	 * 昵称
	 */
	@Column(name = "nickName", nullable = false)
	private String nickName;

	/**
	 * 真实姓名
	 */
	@Column(name = "trueName")
	private String trueName;

	/**
	 * 性别(1:男,2:女,3:保密)
	 */
	@Column(name = "sex", columnDefinition = "char(1) default 3", length = 1, nullable = false)
	private String sex = "3";

	/**
	 * 头像
	 */
	@Column(name = "photo")
	private String photo;

	/**
	 * 是否实名认证(0:否,1:是,2:待审核)
	 */
	@Column(name = "isReal", columnDefinition = "char(1) default 0", length = 1, nullable = false)
	private String isReal = "0";

	/**
	 * 用户详细对象
	 */
	@OneToOne(targetEntity = UserInfo.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "keyId", referencedColumnName = "userId")
	private UserInfo userInfo;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}