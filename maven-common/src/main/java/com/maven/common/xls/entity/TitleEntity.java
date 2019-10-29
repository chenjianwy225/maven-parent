package com.maven.common.xls.entity;

import java.io.Serializable;

/**
 * 标题实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class TitleEntity implements Serializable {

	private static final long serialVersionUID = 3399518488419674926L;

	// 名称
	private String name;

	// 高度
	private short height = 600;

	// 样式名称
	private String styleName;

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            名称
	 */
	public TitleEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
}