package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 业务申请实体
 *
 * @author administrator
 */
public class Business extends BaseEntity {

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
     * 业务名称
     */
    @NotNull(message = "业务名称不能为空")
    @Length(max = 50, message = "业务名称长度不能大于50")
    private String businessName;

    /**
     * 协作部门
     */
    @Length(max = 200, message = "协作部门长度不能大于200")
    private String collaborationDept;

    /**
     * 负责人
     */
    @NotNull(message = "负责人不能为空")
    @Length(max = 200, message = "负责人长度不能大于200")
    private String header;

    /**
     * 开始时间
     */
    @Length(max = 20, message = "开始时间长度不能大于20")
    private String startTims;

    /**
     * 结束时间
     */
    @Length(max = 20, message = "结束时间长度不能大于20")
    private String endTime;

    /**
     * 是否需要场地
     */
    @Length(max = 1, message = "是否需要场地长度不能大于1")
    private String needPlace;

    /**
     * 情况说明
     */
    @NotNull(message = "情况说明不能为空")
    @Length(max = 500, message = "情况说明长度不能大于500")
    private String explains;

    /**
     * 经费
     */
    @NotNull(message = "经费不能为空")
    private Double funds;

    private String senderId;

    private String senderName;

    private String senderDeptName;

    private String collaborationDeptName;

    private String headerName;


    public Business(String id) {
        this.id = id;
    }

    public Business() {
        super();
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCollaborationDept() {
        return collaborationDept;
    }

    public void setCollaborationDept(String collaborationDept) {
        this.collaborationDept = collaborationDept;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getStartTims() {
        return startTims;
    }

    public void setStartTims(String startTims) {
        this.startTims = startTims;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNeedPlace() {
        return needPlace;
    }

    public void setNeedPlace(String needPlace) {
        this.needPlace = needPlace;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public Double getFunds() {
        return funds;
    }

    public void setFunds(Double funds) {
        this.funds = funds;
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

    public String getCollaborationDeptName() {
        return collaborationDeptName;
    }

    public void setCollaborationDeptName(String collaborationDeptName) {
        this.collaborationDeptName = collaborationDeptName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}