package com.kuyuner.bg.article.entity;

import com.kuyuner.common.persistence.BaseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用车实体
 *
 * @author administrator
 */
public class CarHome extends BaseEntity {

    /** 任务 */
    private String taskId;

    /** 流程名称 */
    private String processName;

    /** 车辆信息 */
    private String carNo;

    /** 任务名称 */
    private String taskName;

    /** 操作人（拟稿人） */
    private String sender;

    /** 申请时间 */
    private Date time;

    private String dateStr;

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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(this.time);
        return dateString;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}