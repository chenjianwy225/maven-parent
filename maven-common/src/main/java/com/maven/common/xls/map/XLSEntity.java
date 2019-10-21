package com.maven.common.xls.map;

import java.util.List;

/**
 * XLS实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class XLSEntity {

	// 样式集合
	private List<StyleEntity> styles;

	// 工作簿集合
	private List<SheetEntity> sheets;

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