package com.kuyuner.common.controller;

import java.util.List;

/**
 * @author Administrator
 */
public class ListJson extends AbsResultJson<List<?>> {
    protected int total;

    /**
     * 默认SUCCESS
     */
    public ListJson() {
        super();
    }

    /**
     * 默认SUCCESS
     *
     * @param list
     */
    public ListJson(List<?> list) {
        this(SUCCESS_CODE, null, list);
    }

    public ListJson(int code, List<?> list) {
        this(code, null, list);
    }

    public ListJson(int code, String errorMsg) {
        this(code, errorMsg, null);
    }


    public ListJson(int code, String errorMsg, List<?> list) {
        super(code, list, errorMsg);
        this.total = list == null ? 0 : list.size();
    }

    public int getTotal() {
        return total;
    }
}
