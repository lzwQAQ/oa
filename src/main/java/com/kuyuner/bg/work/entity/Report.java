package com.kuyuner.bg.work.entity;

import com.kuyuner.common.persistence.BaseEntity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.kuyuner.bg.work.entity.Plan;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 工作日报实体
 *
 * @author administrator
 */
public class Report extends BaseEntity {

    /**
     * 任务主键
     */
    private Plan plan;

    /**
     * 工时
     */
    @NotNull(message = "工时不能为空")
    private Double workHour;

    /**
     * 产出
     */
    @Length(max = 500, message = "产出长度不能大于500")
    private String output;

    /**
     * 日报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;


    /**
     * 查询专用字段：日报时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginReportTime;
    /**
     * 查询专用字段：日报时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endReportTime;

    private String chargePeople;

    private String sender;

    public Report(String id) {
        this.id = id;
    }

    public Report() {
        super();
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Double workHour) {
        this.workHour = workHour;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }


    public Date getBeginReportTime() {
        return beginReportTime;
    }

    public void setBeginReportTime(Date beginReportTime) {
        this.beginReportTime = beginReportTime;
    }

    public Date getEndReportTime() {
        return endReportTime;
    }

    public void setEndReportTime(Date endReportTime) {
        this.endReportTime = endReportTime;
    }

    public String getChargePeople() {
        return chargePeople;
    }

    public void setChargePeople(String chargePeople) {
        this.chargePeople = chargePeople;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}