package com.kuyuner.common.controller;


import com.kuyuner.common.persistence.AbsPage;

/**
 * @author Administrator
 */
public class PageJson extends ListJson {

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 总页数
     */
    private int pages;

    public PageJson() {
        super();
    }

    public PageJson(AbsPage<?> page) {
        this(SUCCESS_CODE, null, page);
    }

    public PageJson(int code, AbsPage<?> page) {
        this(code, null, page);
    }

    public PageJson(int code, String message, AbsPage<?> page) {
        super(code, message, page.getList());
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.pages = page.getPages();
        total = page.getTotal();
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPages() {
        return pages;
    }

}
