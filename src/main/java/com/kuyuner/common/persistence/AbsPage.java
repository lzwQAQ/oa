package com.kuyuner.common.persistence;

import java.util.List;

/**
 * @author Administrator
 */
public abstract class AbsPage<T> {
	protected static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 当前页数
	 */
	protected int pageNum;

	/**
	 * 每页大小
	 */
	protected int pageSize;

	/**
	 * 总页数
	 */
	protected int pages;

	/**
	 * 查询数据
	 */
	protected List<T> list;

	/**
	 * 总条数
	 */
	protected int total;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
