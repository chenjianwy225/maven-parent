package com.maven.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 基础实体类
 * 
 * @author chenjian
 * @createDate 2018-12-28
 */
@MappedSuperclass
public class IBaseModel implements Serializable {

	private static final long serialVersionUID = 6181011171699827503L;

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
	private Date createDate;

	/**
	 * 更新日期
	 */
	@Column(name = "modifyDate")
	private Date modifyDate;

	/**
	 * 是否删除(0:否,1:是)
	 */
	@Column(name = "deleteStatus", columnDefinition = "bit default 0", length = 1, nullable = false)
	private Boolean deleteStatus;

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

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
}