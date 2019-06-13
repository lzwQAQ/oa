package com.kuyuner.bg.recanddis.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 发文实体
 *
 * @author administrator
 */
public class ReleaseDocument extends BaseEntity {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空")
    @Length(max = 20, message = "编号长度不能大于20")
    private String numbers;

    /**
     * 密级
     */
    @NotNull(message = "密级不能为空")
    @Length(max = 5, message = "密级长度不能大于5")
    private String secret;

    /**
     * 紧急程度
     */
    @NotNull(message = "紧急程度不能为空")
    @Length(max = 5, message = "紧急程度长度不能大于5")
    private String urgentLevel;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    @Length(max = 200, message = "标题长度不能大于200")
    private String title;

    /**
     * 核稿人
     */
    @Length(max = 50, message = "核稿人长度不能大于50")
    private String examineAuthor;

    /**
     * 附件路径
     */
    @Length(max = 1000, message = "附件路径长度不能大于1000")
    private String filePath;

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
     * 拟稿人姓名
     */
    private String examineAuthorName;

    /**
     * 拟稿部门
     */
    private String examineAuthorDeptName;

    public ReleaseDocument(String id) {
        this.id = id;
    }

    public ReleaseDocument() {
        super();
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExamineAuthor() {
        return examineAuthor;
    }

    public void setExamineAuthor(String examineAuthor) {
        this.examineAuthor = examineAuthor;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getExamineAuthorName() {
        return examineAuthorName;
    }

    public void setExamineAuthorName(String examineAuthorName) {
        this.examineAuthorName = examineAuthorName;
    }

    public String getExamineAuthorDeptName() {
        return examineAuthorDeptName;
    }

    public void setExamineAuthorDeptName(String examineAuthorDeptName) {
        this.examineAuthorDeptName = examineAuthorDeptName;
    }
}