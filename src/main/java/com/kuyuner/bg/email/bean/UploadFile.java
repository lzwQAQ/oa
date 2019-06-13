package com.kuyuner.bg.email.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @create 2018-11-17
 */
public class UploadFile {

    private static final Map<String, String> STATES = new HashMap<>();

    private String name;

    private String originalName;

    private long size;

    private UploadFileState state;

    private String type;

    private String url;


    static {
        //默认成功
        STATES.put("SUCCESS", "SUCCESS");
        STATES.put("NOFILE", "未包含文件上传域");
        STATES.put("TYPE", "不允许的文件格式");
        STATES.put("SIZE", "文件大小超出限制");
        STATES.put("ENTYPE", "请求类型ENTYPE错误");
        STATES.put("REQUEST", "上传请求异常");
        STATES.put("IO", "IO异常");
        STATES.put("DIR", "目录创建失败");
        STATES.put("UNKNOWN", "未知错误");
    }

    public static Map<String, String> getSTATES() {
        return STATES;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public UploadFileState getState() {
        return state;
    }

    public void setState(UploadFileState state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
