package com.kuyuner.bg.email.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 发件箱实体
 *
 * @author administrator
 */
public class EmailSend extends Email {

    /**
     * 密送人名称
     */
    @Length(max = 32, message = "密送人名称长度不能大于32")
    private String secretSenderName;

    /**
     * 密送人账号
     */
    @Length(max = 32, message = "密送人账号长度不能大于32")
    private String secretSenderAccount;

    /**
     * 短信通知
     */
    @NotNull(message = "短信通知不能为空")
    @Length(max = 1, message = "短信通知长度不能大于1")
    private String toMessage;

    /**
     * 是否草稿
     */
    @NotNull(message = "是否草稿不能为空")
    @Length(max = 1, message = "是否草稿长度不能大于1")
    private String draft;

    /**
     * 是否定时邮件
     */
    @NotNull(message = "是否定时邮件不能为空")
    @Length(max = 1, message = "是否定时邮件长度不能大于1")
    private String isSchedule;

    private String emailType = "send";

    public EmailSend(String id) {
        this.id = id;
    }

    public EmailSend() {
        super();
    }

    public String getSecretSenderName() {
        return secretSenderName;
    }

    public void setSecretSenderName(String secretSenderName) {
        this.secretSenderName = secretSenderName;
    }

    public String getSecretSenderAccount() {
        return secretSenderAccount;
    }

    public void setSecretSenderAccount(String secretSenderAccount) {
        this.secretSenderAccount = secretSenderAccount;
    }

    public String getToMessage() {
        return toMessage;
    }

    public void setToMessage(String toMessage) {
        this.toMessage = toMessage;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public String getIsSchedule() {
        return isSchedule;
    }

    public void setIsSchedule(String isSchedule) {
        this.isSchedule = isSchedule;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}