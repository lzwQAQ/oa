package com.kuyuner.bg.work.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 任务计划实体
 *
 * @author administrator
 */
public class Plan extends BaseEntity {

    /**
     * 任务名称
     */
    @NotNull(message = "任务名称不能为空")
    @Length(max = 200, message = "任务名称长度不能大于200")
    private String name;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 任务内容
     */
    @NotNull(message = "任务内容不能为空")
    @Length(max = 2000, message = "任务内容长度不能大于2000")
    private String content;

    /**
     * 备注
     */
    @Length(max = 2000, message = "备注长度不能大于2000")
    private String marks;

    /**
     * 任务状态
     */
    @NotNull(message = "任务状态不能为空")
    @Length(max = 5, message = "任务状态长度不能大于5")
    @DictType("PLAN_STATE")
    private String state;

    /**
     * 进度
     */
    @NotNull(message = "进度不能为空")
    private int process;

    private String chargePeople;

    private String sender;

    public Plan(String id) {
        this.id = id;
    }

    public Plan() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
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