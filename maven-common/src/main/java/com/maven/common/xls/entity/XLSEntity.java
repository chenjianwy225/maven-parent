package com.maven.common.xls.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * XLS实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class XLSEntity implements Serializable {

	private static final long serialVersionUID = 8206054496729990990L;

	// 样式集合
	private List<StyleEntity> styles = new ArrayList<StyleEntity>();

	// 工作簿集合
	private List<SheetEntity> sheets = new ArrayList<SheetEntity>();

	public List<StyleEntity> getStyles() {
		return styles;
	}

	public void setStyles(List<StyleEntity> styles) {
		this.styles = styles;
	}

	public List<SheetEntity> getSheets() {
		return sheets;
	}

	public void setSheets(List<SheetEntity> sheets) {
		this.sheets = sheets;
	}
}