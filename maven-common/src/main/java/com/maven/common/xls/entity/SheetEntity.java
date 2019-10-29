package com.maven.common.xls.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作簿实体类
 * 
 * @author chenjian
 * @createDate 2019-10-18
 */
public class SheetEntity implements Serializable {

	private static final long serialVersionUID = 4064347468868958296L;

	// 名称
	private String name;

	// 下拉列表集合
	private List<TextListEntity> texts = new ArrayList<TextListEntity>();

	// 标题集合
	private List<TitleEntity> titles = new ArrayList<TitleEntity>();

	// 行数据集合
	private List<RowEntity> rows = new ArrayList<RowEntity>();

	/**
	 * 构造函数
	 * 
	 * @param name
	 *            名称
	 */
	public SheetEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<TextListEntity> getTexts() {
		return texts;
	}

	public void setTexts(List<TextListEntity> texts) {
		this.texts = texts;
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