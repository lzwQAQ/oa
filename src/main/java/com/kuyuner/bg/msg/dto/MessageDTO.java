package com.kuyuner.bg.msg.dto;

import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 收文实体
 *
 * @author administrator
 */
public class MessageDTO extends BaseEntity {

    private String id;

    private String content;

    private String schedule;

    private String  scheduleTime;

    /**
     * 发送返回结果（验证是否连接短信客户端是否成功），无结果表示连接成功
     */
    private String sendMsg;

    /**
     * 多个收信人用","分隔
     */
    private String receiver;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }
}