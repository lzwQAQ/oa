package com.kuyuner.bg.approval.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.excel.annotation.ExcelField;
import com.kuyuner.common.persistence.BaseEntity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 采购申请实体
 *
 * @author administrator
 */
public class Accounts extends BaseEntity {

    /**
     * 业务名称
     */
    @ExcelField(title = "业务名称", sort = 10)
    private String name;

    @ExcelField(title = "费用支出/元", sort = 20)
    private String totalPrice;

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

    public Accounts(String id) {
        this.id = id;
    }

    public Accounts() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
}