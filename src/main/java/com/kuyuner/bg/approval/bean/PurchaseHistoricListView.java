package com.kuyuner.bg.approval.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;

/**
 * @author administrator
 */
public class PurchaseHistoricListView extends BaseEntity {

    /**
     * 流程实例ID
     */
    private String processInstanceId;
    /**
     * 流程名称
     */
    private String processName;

    /**
     * 当前环节
     */
    private String taskName;

    /**
     * 申请人
     */
    private String sender;

    private String name;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
