package com.kuyuner.common.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageHelper;

import org.apache.commons.lang3.StringUtils;

/**
 * 自定义分页对象
 *
 * @author Administrator
 */
public class Page<E> extends AbsPage<E> {
    @JsonIgnore
    private com.github.pagehelper.Page<E> page;

    public Page(String pageNum) {
        this(pageNum, null);
    }

    public Page(String pageNum, String pageSize) {
        int pNum = 1;
        int pSize = DEFAULT_PAGE_SIZE;
        if (StringUtils.isNumeric(pageNum)) {
            pNum = Integer.valueOf(pageNum);
        }
        if (StringUtils.isNumeric(pageSize)) {
            pSize = Integer.valueOf(pageSize);
        }
        this.pageNum = pNum;
        this.pageSize = pSize;
    }

    public Page(int pageNum) {
        this(pageNum, DEFAULT_PAGE_SIZE);
    }

    public Page(int pageNum, int pageSize) {
        if (pageNum <= 0) {
            throw new RuntimeException("pageNum can not be smaller than 0");
        }

        if (pageSize <= 0) {
            throw new RuntimeException("pageSize can not be smaller than 0");
        }

        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    /**
     * 开始分页
     */
    @SuppressWarnings("unchecked")
    public void start() {
        page = PageHelper.startPage(pageNum, pageSize, true);
    }

    public void end() {
        this.total = (int) page.getTotal();
        this.pages = page.getPages();
        this.list = page.getResult();
        page = null;
    }
}
