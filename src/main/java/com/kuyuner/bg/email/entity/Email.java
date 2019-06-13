package com.kuyuner.bg.email.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 邮件公共信息
 *
 * @author administrator
 */
public class Email extends BaseEntity {

    /**
     * 主题
     */
    @NotNull(message = "主题不能为空")
    @Length(max = 200, message = "主题长度不能大于200")
    private String title;

    /**
     * 正文
     */
    @NotNull(message = "正文不能为空")
    private String content;

    /**
     * 发件人名称
     */
    @NotNull(message = "发件人名称不能为空")
    @Length(max = 32, message = "发件人名称长度不能大于32")
    private String senderName;

    /**
     * 发件人账号
     */
    @NotNull(message = "发件人账号不能为空")
    @Length(max = 32, message = "发件人账号长度不能大于32")
    private String senderAccount;

    /**
     * 收件人名称
     */
    @NotNull(message = "收件人名称不能为空")
    @Length(max = 32, message = "收件人名称长度不能大于32")
    private String receiverName;

    /**
     * 收件人账号
     */
    @NotNull(message = "收件人账号不能为空")
    @Length(max = 32, message = "收件人账号长度不能大于32")
    private String receiverAccount;

    /**
     * 抄送人名称
     */
    @Length(max = 32, message = "抄送人名称长度不能大于32")
    private String copySenderName;

    /**
     * 抄送人账号
     */
    @Length(max = 32, message = "抄送人账号长度不能大于32")
    private String copySenderAccount;


    /**
     * 包含附件
     */
    @NotNull(message = "包含附件不能为空")
    @Length(max = 1, message = "包含附件长度不能大于1")
    private String containFile;

    /**
     * 是否收藏
     */
    @Length(max = 1, message = "是否收藏长度不能大于1")
    private String star;

    /**
     * 等级
     */
    @NotNull(message = "等级不能为空")
    @Length(max = 1, message = "等级长度不能大于1")
    private String level;

    /**
     * 种类
     */
    @Length(max = 5, message = "种类长度不能大于5")
    @DictType("EMAIL_TYPE")
    private String type;

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
}
