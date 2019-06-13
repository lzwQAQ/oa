package com.kuyuner.bg.approval.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.excel.annotation.ExcelField;
import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 采购申请实体
 *
 * @author administrator
 */
public class Purchase extends BaseEntity {

    /**
     * 业务名称
     */
    @ExcelField(title = "业务名称", sort = 10)
    private String name;

    /**
     * 采购事由
     */
    @Length(max = 600, message = "采购事由长度不能大于600")
    private String reason;

    @ExcelField(title = "费用支出/元", sort = 20)
    private String totalPrice;

    /**
     * 当前环节
     */
    @Length(max = 50, message = "当前环节长度不能大于50")
    private String taskName;

    /**
     * 审批结果
     */
    private String approvalResult;

    /**
     * 发送人Id
     */
    private String senderId;

    /**
     * 发送人
     */
    @ExcelField(title = "申请人", sort = 30)
    private String senderName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "申请时间", sort = 50, dataFormat = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 所属部门
     */
    @ExcelField(title = "申请部门", sort = 40)
    private String senderDeptName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginSendTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSendTime;

    public Purchase(String id) {
        this.id = id;
    }

    public Purchase() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public Date getBeginSendTime() {
        return beginSendTime;
    }

    public void setBeginSendTime(Date beginSendTime) {
        this.beginSendTime = beginSendTime;
    }

    public Date getEndSendTime() {
        return endSendTime;
    }

    public void setEndSendTime(Date endSendTime) {
        this.endSendTime = endSendTime;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}