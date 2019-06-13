package com.kuyuner.bg.recanddis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 收文实体
 *
 * @author administrator
 */
public class ReceiveDocument extends BaseEntity {

    /**
     * 发文机构
     */
    @NotNull(message = "发文机构不能为空")
    private String releaseOrg;

    /**
     * 收文时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "收文时间不能为空")
    private Date receiveDate;

    /**
     * 收文类型
     */
    @NotNull(message = "收文类型不能为空")
    @Length(max = 5, message = "收文类型长度不能大于5")
    private String receiveType;

    /**
     * 收文号
     */
    @NotNull(message = "收文号不能为空")
    @Length(max = 10, message = "收文号长度不能大于10")
    private String receiveNum;

    /**
     * 来文号
     */
    @NotNull(message = "来文号不能为空")
    @Length(max = 10, message = "来文号长度不能大于10")
    private String incomeNum;

    /**
     * 密级
     */
    @NotNull(message = "密级不能为空")
    @Length(max = 5, message = "密级长度不能大于5")
    private String secret;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    @Length(max = 200, message = "标题长度不能大于200")
    private String title;

    /**
     * 备注
     */
    @Length(max = 520, message = "备注长度不能大于520")
    private String marks;

    /**
     * 当前环节
     */
    @Length(max = 50, message = "当前环节长度不能大于50")
    private String taskName;

    /**
     * 处理意见
     */
    private String approvalResult;

    /**
     * 申请人
     */
    @NotNull(message = "申请人不能为空")
    @Length(max = 32, message = "申请人长度不能大于32")
    private String senderId;

    /**
     * 发送人
     */
    private String senderName;

    /**
     * 所属部门
     */
    private String senderDeptName;

    /**
     * 发文机构名称
     */
    private String releaseOrgName;

    public ReceiveDocument(String id) {
        this.id = id;
    }

    public ReceiveDocument() {
        super();
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(String receiveNum) {
        this.receiveNum = receiveNum;
    }

    public String getIncomeNum() {
        return incomeNum;
    }

    public void setIncomeNum(String incomeNum) {
        this.incomeNum = incomeNum;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
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

    public String getReleaseOrg() {
        return releaseOrg;
    }

    public void setReleaseOrg(String releaseOrg) {
        this.releaseOrg = releaseOrg;
    }

    public String getReleaseOrgName() {
        return releaseOrgName;
    }

    public void setReleaseOrgName(String releaseOrgName) {
        this.releaseOrgName = releaseOrgName;
    }
}