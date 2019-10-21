package com.maven.common.xls.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Row实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class RowEntity {

	// 高度
	private short height = 300;

	// 单元格数据集合
	private List<CellEntity> cells = new ArrayList<CellEntity>();

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public List<CellEntity> getCells() {
		return cells;
	}

	public void setCells(List<CellEntity> cells) {
		this.cells = cells;
	}
}