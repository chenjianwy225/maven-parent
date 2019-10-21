package com.maven.common.xls.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Sheet实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class SheetEntity {

	// 名称
	private String name;

	// 标题集合
	private List<TitleEntity> titles = new ArrayList<TitleEntity>();

	// 行数据集合
	private List<RowEntity> rows = new ArrayList<RowEntity>();

	public SheetEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TitleEntity> getTitles() {
		return titles;
	}

	public void setTitles(List<TitleEntity> titles) {
		this.titles = titles;
	}

	public List<RowEntity> getRows() {
		return rows;
	}

	public void setRows(List<RowEntity> rows) {
		this.rows = rows;
	}
}