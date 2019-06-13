package com.kuyuner.bg.article.entity;

import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 收文实体
 *
 * @author administrator
 */
public class EmailHome extends BaseEntity {

    /** 任务 */
    private String id;

    private String title;

    private String senderName;

    private String senderAccount;

    private String receiverName;

    private String receiverAccount;

    private String copySenderName;

    private String copySenderAccount;

    private String containFile;

    private String star;

    private String type;

    private String level;

    private String read;

    private String isSecret;

    private String creater;

    private Date createDate;

    private Date updateDate;

    private String delFlag;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getCopySenderName() {
        return copySenderName;
    }

    public void setCopySenderName(String copySenderName) {
        this.copySenderName = copySenderName;
    }

    public String getCopySenderAccount() {
        return copySenderAccount;
    }

    public void setCopySenderAccount(String copySenderAccount) {
        this.copySenderAccount = copySenderAccount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    @Override
    public String getCreater() {
        return creater;
    }

    @Override
    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public void setCreater(String creater) {
        this.creater = creater;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    @Override
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}