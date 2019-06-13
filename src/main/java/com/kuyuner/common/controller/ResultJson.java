package com.kuyuner.common.controller;

/**
 * @author Administrator
 */
public class ResultJson extends AbsResultJson<Object> {

    /**
     * 默认结果是成功的
     */
    public ResultJson() {
        super();
    }

    public ResultJson(int code) {
        super(code);
    }

    public ResultJson(int code, Object data, String errorMsg) {
        super(code, data, errorMsg);
    }

    public static ResultJson ok() {
        return new ResultJson(SUCCESS_CODE);
    }

    public static ResultJson ok(Object data) {
        return new ResultJson(SUCCESS_CODE, data, null);
    }

    public static ResultJson failed() {
        return new ResultJson(FAIL_CODE);
    }

    public static ResultJson failed(String errorMsg) {
        return new ResultJson(FAIL_CODE, null, errorMsg);
    }
}
