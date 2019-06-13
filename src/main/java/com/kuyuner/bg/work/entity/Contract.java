package com.kuyuner.bg.work.entity;

import com.kuyuner.common.persistence.BaseEntity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 合同管理实体
 *
 * @author administrator
 */
public class Contract extends BaseEntity {

    /**
     * 合同名称
     */
    @NotNull(message = "合同名称不能为空")
    @Length(max = 100, message = "合同名称长度不能大于100")
    private String name;

    /**
     * 签约公司
     */
    @Length(max = 100, message = "签约公司长度不能大于100")
    private String signingCompany;

    /**
     * 签约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date signingDate;

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
    private Long fileSize;

    /**
     * 文件路径
     */
    @NotNull(message = "文件路径不能为空")
    @Length(max = 500, message = "文件路径长度不能大于500")
    private String filePath;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginSigningDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSigningDate;

    public Contract(String id) {
        this.id = id;
    }

    public Contract() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSigningCompany() {
        return signingCompany;
    }

    public void setSigningCompany(String signingCompany) {
        this.signingCompany = signingCompany;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getBeginSigningDate() {
        return beginSigningDate;
    }

    public void setBeginSigningDate(Date beginSigningDate) {
        this.beginSigningDate = beginSigningDate;
    }

    public Date getEndSigningDate() {
        return endSigningDate;
    }

    public void setEndSigningDate(Date endSigningDate) {
        this.endSigningDate = endSigningDate;
    }
}