package com.kuyuner.workflow.manage.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author tangzj
 */
public class ModelInfo {

    private String modelId;
    private String modelKey;
    private String modelName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modelCreateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modelLastUpdateTime;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Date getModelCreateTime() {
        return modelCreateTime;
    }

    public void setModelCreateTime(Date modelCreateTime) {
        this.modelCreateTime = modelCreateTime;
    }

    public Date getModelLastUpdateTime() {
        return modelLastUpdateTime;
    }

    public void setModelLastUpdateTime(Date modelLastUpdateTime) {
        this.modelLastUpdateTime = modelLastUpdateTime;
    }
}
