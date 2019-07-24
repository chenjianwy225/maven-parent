package com.maven.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户详细信息
 * 
 * @author chenjian
 * @createDate 2019-01-11
 */
@Entity
@Table(name = "f_user_info")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 6644713526414205388L;

	public static final String tableName = "f_user_info";

	/**
	 * 用户编号
	 */
	@Id
	@Column(unique = true, nullable = false)
	private String userId;

	/**
	 * 证件类型(1:身份证,2:军官证)
	 */
	@Column(name = "idType")
	private String idType;

	/**
	 * 证件号码
	 */
	@Column(name = "idNo")
	private String idNo;

	/**
	 * 个性签名
	 */
	@Column(name = "idiograph")
	private String idiograph;

	/**
	 * QQ号码
	 */
	@Column(name = "qqNo")
	private String qqNo;

	/**
	 * 微信号码
	 */
	@Column(name = "weiXin")
	private String weiXin;

	/**
	 * 微博号码
	 */
	@Column(name = "weiBo")
	private String weiBo;

	/**
	 * 职业
	 */
	@Column(name = "occupation")
	private String occupation;

	/**
	 * 年龄
	 */
	@Column(name = "age", columnDefinition = "int default 0")
	private int age = 0;

	/**
	 * 出生日期
	 */
	@Column(name = "birthday")
	private Date birthday;

	/**
	 * 星座
	 */
	@Column(name = "constellation")
	private String constellation;

	/**
	 * 学位
	 */
	@Column(name = "degree")
	private String degree;

	/**
	 * 毕业学校
	 */
	@Column(name = "school")
	private String school;

	/**
	 * 公司(单位)名称
	 */
	@Column(name = "company")
	private String company;

	/**
	 * 住址
	 */
	@Column(name = "address")
	private String address;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIdiograph() {
		return idiograph;
	}

	public void setIdiograph(String idiograph) {
		this.idiograph = idiograph;
	}

	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getWeiBo() {
		return weiBo;
	}

	public void setWeiBo(String weiBo) {
		this.weiBo = weiBo;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}