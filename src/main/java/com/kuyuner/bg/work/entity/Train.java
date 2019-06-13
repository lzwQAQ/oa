package com.kuyuner.bg.work.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.sys.entity.User;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 培训通知实体
 *
 * @author administrator
 */
public class Train extends BaseEntity {

    /**
     * 培训主题
     */
    @NotNull(message = "培训主题不能为空")
    @Length(max = 200, message = "培训主题长度不能大于200")
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
     * 培训时长
     */
    private String duration;

    /**
     * 培训地点
     */
    @NotNull(message = "培训地点不能为空")
    @Length(max = 200, message = "培训地点长度不能大于200")
    private String place;

    /**
     * 培训人员
     */
    @NotNull(message = "培训人员不能为空")
    @Length(max = 32, message = "培训人员长度不能大于32")
    private User trainer;

    /**
     * 参与人员
     */
    @Length(max = 4000, message = "参与人员长度不能大于2000")
    private String joinPeople;

    /**
     * 培训内容
     */
    @NotNull(message = "培训内容不能为空")
    @Length(max = 2000, message = "培训内容长度不能大于2000")
    private String content;

    /**
     * 备注
     */
    @Length(max = 2000, message = "备注长度不能大于2000")
    private String marks;

    /**
     * 发送标记（1：表示发送；0：表示接受）
     */
    @NotNull(message = "发送标记不能为空")
    @Length(max = 1, message = "1：表示发送；0：表示接受长度不能大于1")
    private String trainType;

    public Train(String id) {
        this.id = id;
    }

    public Train() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }


    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
}