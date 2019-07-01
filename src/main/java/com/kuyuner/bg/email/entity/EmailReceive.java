package com.kuyuner.bg.email.entity;

/**
 * 收件箱实体
 *
 * @author administrator
 */
public class EmailReceive extends Email {
    /**
     * 是否阅读
     */
    private String read;

    /**
     * 是否是密送邮件
     */
    private String isSecret;

    private String emailType = "receive";

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public EmailReceive() {
    }

    public EmailReceive(String id) {
        super();
        this.id = id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getIsSecret() {
        return isSecret;
    }

    public void setIsSecret(String isSecret) {
        this.isSecret = isSecret;
    }
}