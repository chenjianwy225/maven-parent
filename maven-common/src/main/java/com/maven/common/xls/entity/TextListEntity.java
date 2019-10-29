package com.maven.common.xls.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 下拉实体类
 * 
 * @author chenjian
 * @createDate 2019-10-23
 */
public class TextListEntity implements Serializable {

	private static final long serialVersionUID = -2986835951998453054L;

	// 数据集合
	private List<String> datas = new ArrayList<String>();

	// 开始行
	private int firstRow = 0;

	// 结束行
	private int lastRow = 0;

	// 开始列
	private int firstColumn = 0;

	// 结束列
	private int lastColumn = 0;

	/**
	 * 构造函数
	 * 
	 * @param datas
	 *            数据集合
	 */
	public TextListEntity(List<String> datas) {
		this.datas = datas;
	}

	public TextListEntity(List<String> datas, int firstRow, int lastRow,
			int firstColumn, int lastColumn) {
		this.datas = datas;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.firstColumn = firstColumn;
		this.lastColumn = lastColumn;
	}

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int getFirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(int firstColumn) {
		this.firstColumn = firstColumn;
	}

	public int getLastColumn() {
		return lastColumn;
	}

	public void setLastColumn(int lastColumn) {
		this.lastColumn = lastColumn;
	}
}