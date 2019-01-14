package com.maven.common.page;

import java.util.List;

/**
 * 分页类
 * 
 * @author chenjian
 * @createDate 2019-01-03
 */
public class Pager {

	// 当前页数
	private int currentPage = 1;

	// 总页数
	private int pages;

	// 每页显示条数
	private int pageSize = 20;

	// 总条数
	private int rowsTotal;

	// 分页语句
	private String hql;

	// 数据集合
	private List<?> list;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRowsTotal() {
		return rowsTotal;
	}

	public void setRowsTotal(int rowsTotal) {
		this.rowsTotal = rowsTotal;
		this.pages = rowsTotal % this.pageSize == 0 ? rowsTotal / this.pageSize
				: rowsTotal / this.pageSize + 1;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}