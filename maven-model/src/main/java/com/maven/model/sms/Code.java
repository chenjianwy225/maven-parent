package com.maven.model.sms;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 验证码实体类
 * 
 * @author chenjian
 * @createDate 2019-09-12
 */
@Entity
@Table(name = "f_code")
public class Code implements Serializable {

	private static final long serialVersionUID = -6947475485355216931L;

	public static final String tableName = "f_code";

	/**
	 * 手机号码(主键)
	 */
	@Id
	@Column(name = "mobile", unique = true, nullable = false)
	private String mobile;

	/**
	 * 验证码
	 */
	@Column(name = "codeNum", nullable = false)
	private String codeNum;

	/**
	 * 发送次数
	 */
	@Column(name = "number", columnDefinition = "int default 0", nullable = false)
	private int number = 0;

	/**
	 * 过期时间(分钟)
	 */
	@Column(name = "expireTime", columnDefinition = "int default 0", nullable = false)
	private int expireTime = 0;

	/**
	 * 开始日期
	 */
	@Column(name = "startDate", nullable = false)
	private Date startDate;

	/**
	 * 结束日期
	 */
	@Column(name = "endDate", nullable = false)
	private Date endDate;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}