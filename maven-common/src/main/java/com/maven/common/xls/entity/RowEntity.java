package com.maven.common.xls.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 行实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class RowEntity implements Serializable {

	private static final long serialVersionUID = 814731542716078757L;

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