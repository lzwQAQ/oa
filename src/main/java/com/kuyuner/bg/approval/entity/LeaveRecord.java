package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.excel.annotation.ExcelField;
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
public class LeaveRecord extends BaseEntity {

    /**
     * 请假类型
     */
    @NotNull(message = "请假类型不能为空")
    @Length(max = 5, message = "请假类型长度不能大于5")
    @DictType("LEAVE_TYPE")
    @ExcelField(title = "请假类型", sort = 30)
    private String type;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @Length(max = 20, message = "开始时间长度不能大于20")
    @ExcelField(title = "请假开始时间", sort = 50)
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @Length(max = 20, message = "结束时间长度不能大于20")
    @ExcelField(title = "请假结束时间", sort = 60)
    private String endTime;

    /**
     * 请假天数
     */
    @ExcelField(title = "请假天数", sort = 40)
    private Double leaveDay;

    /**
     * 申请人ID
     */
    private String senderId;
    
    /**
     * 发送人
     */
    @ExcelField(title = "申请人", sort = 10)
    private String senderName;

    /**
     * 所属部门
     */
    @ExcelField(title = "申请部门", sort = 20)
    private String senderDeptName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginLeaveTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endLeaveTime;

    public LeaveRecord(String id) {
        this.id = id;
    }

    public LeaveRecord() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Double getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(Double leaveDay) {
        this.leaveDay = leaveDay;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}