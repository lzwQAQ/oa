package com.kuyuner.bg.email.entity;

import com.kuyuner.common.lang.StringUtils;
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

    private String receiveUnion = "";

    private String senderUnion = "";

    private String copysUnion = "";

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

    public String getReceiveUnion() {
        String str = "";
        if (StringUtils.isNotBlank(this.getReceiverName())) {
            String[] recv = this.getReceiverName().split(";");
            String[] recvAcc = this.getReceiverAccount().split(";");
            for(int i = 0; i < recv.length; i++){
                if (i == recv.length - 1) {
                    str = str + recv[i] + ";" + recvAcc[i] ;
                }else {
                    str = str + recv[i] + ";" + recvAcc[i]+ ",";
                }
            }
        }
        return str;
    }

    public void setReceiveUnion(String receiveUnion) {
        this.receiveUnion = receiveUnion;
    }

    public String getSenderUnion() {
        String str = "";
        if (StringUtils.isNotBlank(this.getSenderName())) {
            String[] send = this.getSenderName().split(";");
            String[] sendAcc = this.getSenderAccount().split(";");
            for(int i = 0; i < send.length; i++){
                if (i == send.length - 1) {
                    str = str + send[i] + ";" + sendAcc[i];
                }else {
                    str = str + send[i] + ";" + sendAcc[i] + ",";
                }
            }
        }
        return str;
    }

    public void setSenderUnion(String senderUnion) {
        this.senderUnion = senderUnion;
    }

    public String getCopysUnion() {
        String str = "";
        if (StringUtils.isNotBlank(this.getCopySenderName())) {
            String[] copy = this.getCopySenderName().split(";");
            String[] copyAcc = this.getCopySenderAccount().split(";");
            for(int i = 0; i < copy.length; i++){
                if (i == copy.length - 1) {
                    str = str + copy[i] + ";" + copyAcc[i];
                }else {
                    str = str + copy[i] + ";" + copyAcc[i]  + ",";
                }
            }
        }
        return str;
    }

    public void setCopysUnion(String copysUnion) {
        this.copysUnion = copysUnion;
    }
}