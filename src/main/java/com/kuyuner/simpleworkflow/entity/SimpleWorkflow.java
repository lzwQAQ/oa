package com.kuyuner.simpleworkflow.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

/**
 * 简单流程实体
 *
 * @author administrator
 */
public class SimpleWorkflow extends BaseEntity {

    /**
     * 标题
     */
    @Length(max = 50, message = "标题长度不能大于50")
    private String title;

    /**
     * 内容
     */
    @Length(max = 1200, message = "内容长度不能大于1200")
    private String content;

    /**
     * 所属流程模型
     */
    private String modelId;

    /**
     * 当前环节
     */
    private String taskName;

    /**
     * 审批结果
     */
    private String approvalResult;

    private String senderId;

    private String senderName;

    private String senderDeptName;

    public SimpleWorkflow(String id) {
        this.id = id;
    }

    public SimpleWorkflow() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
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