package com.kuyuner.common.io;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;

/**
 * @author Administrator
 * @create 2018-11-15
 */
public class FileInfo {

    /**
     * 文件名
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件后缀
     */
    private String suffix;

    @JsonIgnore
    private File originFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public File getOriginFile() {
        return originFile;
    }

    public void setOriginFile(File originFile) {
        this.originFile = originFile;
    }
}
