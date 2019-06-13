package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 邮箱复件实体
 *
 * @author administrator
 */
public class ApprovalFile extends BaseEntity {

    /**
     * 审批外键
     */
    @NotNull(message = "发送邮件外键不能为空")
    @Length(max = 32, message = "发送邮件外键长度不能大于32")
    private String appId;

    /**
     * 文件名称
     */
    @NotNull(message = "文件名称不能为空")
    @Length(max = 200, message = "文件名称长度不能大于200")
    private String fileName;

    /**
     * 文件大小
     */
    @NotNull(message = "文件大小不能为空")
    @Length(max = 11, message = "文件大小长度不能大于11")
    private long fileSize;

    /**
     * 文件路径
     */
    @NotNull(message = "文件路径不能为空")
    @Length(max = 500, message = "文件路径长度不能大于500")
    private String filePath;


    public ApprovalFile(String id) {
        this.id = id;
    }

    public ApprovalFile() {
        super();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}