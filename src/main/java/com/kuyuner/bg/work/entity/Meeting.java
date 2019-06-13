package com.kuyuner.bg.work.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;
import com.kuyuner.core.sys.entity.User;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 会议通知实体
 *
 * @author administrator
 */
public class Meeting extends BaseEntity {

    /**
     * 会议主题
     */
    @NotNull(message = "会议主题不能为空")
    @Length(max = 200, message = "会议主题长度不能大于200")
    private String title;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 会议时长
     */
    @NotNull(message = "会议时长不能为空")
    private String duration;

    /**
     * 会议地点
     */
    @NotNull(message = "会议地点不能为空")
    @Length(max = 200, message = "会议地点长度不能大于200")
    private String place;

    /**
     * 会议发起人
     */
    @NotNull(message = "会议发起人不能为空")
    @Length(max = 32, message = "会议发起人长度不能大于32")
    private User trainer;

    /**
     * 参与人员
     */
    @Length(max = 4000, message = "参与人员长度不能大于2000")
    private String joinPeople;

    /**
     * 会议内容
     */
    @NotNull(message = "会议内容不能为空")
    @Length(max = 2000, message = "会议内容长度不能大于2000")
    private String content;

    /**
     * 备注
     */
    @Length(max = 2000, message = "备注长度不能大于2000")
    private String marks;

    /**
     * 1：表示发送；0：表示接受
     */
    @NotNull(message = "1：表示发送；0：表示接受不能为空")
    @Length(max = 1, message = "1：表示发送；0：表示接受长度不能大于1")
    private String meetingType;

    /**
     * 1：发送短信；0：无短信
     */
    @NotNull(message = "1：发送短信；0：无短信不能为空")
    @Length(max = 1, message = "1：发送短信；0：无短信长度不能大于1")
    private String isSendMessage;

    /**
     * 会议类型
     */
    @NotNull(message = "会议类型不能为空")
    @Length(max = 5, message = "会议类型长度不能大于5")
    @DictType("MEETING_TYPE")
    private String type;

    public Meeting(String id) {
        this.id = id;
    }

    public Meeting() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public String getJoinPeople() {
        return joinPeople;
    }

    public void setJoinPeople(String joinPeople) {
        this.joinPeople = joinPeople;
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

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getIsSendMessage() {
        return isSendMessage;
    }

    public void setIsSendMessage(String isSendMessage) {
        this.isSendMessage = isSendMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}