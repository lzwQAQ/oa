package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 财务申请实体
 *
 * @author administrator
 */
public class Finance extends BaseEntity {

    /**
     * 业务名称
     */
    @NotNull(message = "业务名称不能为空")
    @Length(max = 50, message = "业务名称长度不能大于50")
    private String business;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    private Double money;

    /**
     * 用途
     */
    @NotNull(message = "用途不能为空")
    @Length(max = 200, message = "用途长度不能大于200")
    private String purpose;

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


    public Finance(String id) {
        this.id = id;
    }

    public Finance() {
        super();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
}