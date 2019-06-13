package com.kuyuner.bg.recanddis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 收文实体
 *
 * @author administrator
 */
public class ReceiveDocument2 extends BaseEntity {

    /**
     * 发文机构
     */
    @NotNull(message = "发文机构不能为空")
    private String releaseOrg;

    /**
     * 收文时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "收文时间不能为空")
    private Date receiveDate;

    /**
     * 紧急程度
     */
    @DictType("SEND_DOC_URGENT")
    private String urgentLevel;

    /**
     * 发文号
     */
    @NotNull(message = "发文号不能为空")
    @Length(max = 10, message = "发文号长度不能大于10")
    private String numbers;

    /**
     * 密级
     */
    @NotNull(message = "密级不能为空")
    @Length(max = 5, message = "密级长度不能大于5")
    @DictType("SECRET_LEVEL")
    private String secret;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    @Length(max = 200, message = "标题长度不能大于200")
    private String title;

    private String star;

    private String releaseOrgName;

    public ReceiveDocument2(String id) {
        this.id = id;
    }

    public ReceiveDocument2() {
        super();
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseOrg() {
        return releaseOrg;
    }

    public void setReleaseOrg(String releaseOrg) {
        this.releaseOrg = releaseOrg;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUrgentLevel() {
        return urgentLevel;
    }

    public void setUrgentLevel(String urgentLevel) {
        this.urgentLevel = urgentLevel;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getReleaseOrgName() {
        return releaseOrgName;
    }

    public void setReleaseOrgName(String releaseOrgName) {
        this.releaseOrgName = releaseOrgName;
    }
}