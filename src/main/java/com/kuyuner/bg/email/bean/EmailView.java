package com.kuyuner.bg.email.bean;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

/**
 * @author Administrator
 * @create 2018-11-24
 */
public class EmailView extends BaseEntity {

    /**
     * 主题
     */
    private String title;

    /**
     * 发件人名称
     */
    private String senderName;

    /**
     * 发件人账号
     */
    private String senderAccount;

    /**
     * 包含附件
     */
    private String containFile;

    /**
     * 是否收藏
     */
    private String star;

    /**
     * 等级
     */
    private String level;

    /**
     * 种类
     */
    @DictType("EMAIL_TYPE")
    private String type;

    /**
     * 是否阅读
     */
    private String read;

    private String emailType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getContainFile() {
        return containFile;
    }

    public void setContainFile(String containFile) {
        this.containFile = containFile;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
