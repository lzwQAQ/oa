package com.kuyuner.workflow.manage.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;

/**
 * @author tangzj
 */
public class Deployment extends BaseEntity {
    private String deploymentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String procdefId;
    private String name;
    private int version;
    private String key;
    private String resourceName;
    private String dgrmResourceName;

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getProcdefId() {
        return procdefId;
    }

    public void setProcdefId(String procdefId) {
        this.procdefId = procdefId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDgrmResourceName() {
        return dgrmResourceName;
    }

    public void setDgrmResourceName(String dgrmResourceName) {
        this.dgrmResourceName = dgrmResourceName;
    }
}
