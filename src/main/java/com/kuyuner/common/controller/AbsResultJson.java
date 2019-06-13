package com.kuyuner.common.controller;

/**
 * @author administrator
 */
public abstract class AbsResultJson<T> {
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    /**
     * 200：成功，500：失败
     */
    protected int code;
    protected String errorMsg;
    protected T data;

    public AbsResultJson(int code) {
        this.code = code;
    }

    public AbsResultJson(int code, T data, String errorMsg) {
        this.code = code;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    /**
     * 默认结果是成功的
     */
    public AbsResultJson() {
        code = SUCCESS_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
