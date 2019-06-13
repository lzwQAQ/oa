package com.kuyuner.bg.article.entity;

import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 收文实体
 *
 * @author administrator
 */
public class PurchaseHome extends BaseEntity {

    /** 任务 */
    private String taskId;

    /** 流程名称 */
    private String processName;

    /** 任务名称 */
    private String taskName;

    /** 操作人（请假人） */
    private String sender;

    /** 所属部门 */
    private String deptName;

    /** 申请时间 */
    private Date time;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}