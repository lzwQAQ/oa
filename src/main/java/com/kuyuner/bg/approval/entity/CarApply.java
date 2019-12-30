package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 车辆申请实体
 *
 * @author administrator
 */
public class CarApply extends BaseEntity {

    /**
     * 车辆
     */
    @NotNull(message = "车辆不能为空")
    private Car car;

    /**
     * 驾驶员
     */
    @NotNull(message = "驾驶员不能为空")
    @Length(max = 32, message = "驾驶员长度不能大于32")
    private User driver;

    /**
     * 用车人数
     */
    @NotNull(message = "用车人数不能为空")
    @Length(max = 4, message = "用车人数长度不能大于4")
    private Integer people;

    /**
     * 用车地点
     */
    @NotNull(message = "用车地点不能为空")
    @Length(max = 200, message = "用车地点长度不能大于200")
    private String address;

    /**
     * 用车时间
     */
    @NotNull(message = "用车时间不能为空")
    @Length(max = 20, message = "用车时间长度不能大于20")
    private String startTime;

    /**
     * 还车时间
     */
    @NotNull(message = "还车时间不能为空")
    @Length(max = 20, message = "还车时间长度不能大于20")
    private String endTime;

    /**
     * 用车原因
     */
    @NotNull(message = "用车原因不能为空")
    @Length(max = 120, message = "用车原因长度不能大于120")
    private String reason;

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

    public CarApply(String id) {
        this.id = id;
    }

    public CarApply() {
        super();
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}