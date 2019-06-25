package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 请假实体
 *
 * @author administrator
 */
public class Leave extends BaseEntity {

    /**
     * 请假类型
     */
    @NotNull(message = "请假类型不能为空")
    @Length(max = 5, message = "请假类型长度不能大于5")
    @DictType("LEAVE_TYPE")
    private String type;

    /**
     * 请假事由
     */
    @NotNull(message = "请假事由不能为空")
    @Length(max = 100, message = "请假事由长度不能大于100")
    private String reason;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @Length(max = 20, message = "开始时间长度不能大于20")
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @Length(max = 20, message = "结束时间长度不能大于20")
    private String endTime;

    /**
     * 请假天数
     */
    private Double leaveDay;

    /**
     * 当前环节
     */
    @Length(max = 50, message = "当前环节长度不能大于50")
    private String taskName;

    /**
     * 申请人ID
     */
    private String senderId;

    /**
     * 审批结果
     */
    private String approvalResult;

    /**
     * 发送人
     */
    private String senderName;

    /**
     * 所属部门
     */
    private String senderDeptName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginLeaveTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endLeaveTime;

    public Leave(String id) {
        this.id = id;
    }

    public Leave() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDeptName() {
        return senderDeptName;
    }

    public void setSenderDeptName(String senderDeptName) {
        this.senderDeptName = senderDeptName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Double getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(Double leaveDay) {
        this.leaveDay = leaveDay;
    }

    public Date getBeginLeaveTime() {
        return beginLeaveTime;
    }

    public void setBeginLeaveTime(Date beginLeaveTime) {
        this.beginLeaveTime = beginLeaveTime;
    }

    public Date getEndLeaveTime() {
        return endLeaveTime;
    }

    public void setEndLeaveTime(Date endLeaveTime) {
        this.endLeaveTime = endLeaveTime;
    }
}