package com.kuyuner.bg.approval.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author administrator
 */
public class LeavePendingListView extends BaseEntity {
    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称")
    private String processName;

    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    @DictType("LEAVE_TYPE")
    private String type;

    /**
     * 当前环节
     */
    @ApiModelProperty(value = "当前环节")
    private String taskName;

    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人")
    private String sender;

    @ApiModelProperty(value = "请假天数")
    private double leaveDay;

    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String startTime;

    private String endTime;

    private String reason;

    private String sendDeptName;

    private String leaveType;

    /**
     * 审批意见
     */
    private String approvalOpinion;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(double leaveDay) {
        this.leaveDay = leaveDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSendDeptName() {
        return sendDeptName;
    }

    public void setSendDeptName(String sendDeptName) {
        this.sendDeptName = sendDeptName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getApprovalOpinion() {
        return approvalOpinion;
    }

    public void setApprovalOpinion(String approvalOpinion) {
        this.approvalOpinion = approvalOpinion;
    }
}
