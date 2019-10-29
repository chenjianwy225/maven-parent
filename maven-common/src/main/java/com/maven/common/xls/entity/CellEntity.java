package com.maven.common.xls.entity;

import java.io.Serializable;

/**
 * 单元格实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class CellEntity implements Serializable {

	private static final long serialVersionUID = -4636375039400864460L;

	// 内容值
	private Object value;

	// 宽度
	private int width = 15 * 256;

	// 样式名称
	private String styleName;

	/**
	 * 构造函数
	 * 
	 * @param value
	 *            内容值
	 */
	public CellEntity(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
}