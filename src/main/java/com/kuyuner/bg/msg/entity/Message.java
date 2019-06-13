package com.kuyuner.bg.msg.entity;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.BaseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 收文实体
 *
 * @author administrator
 */
public class Message extends BaseEntity {

    private String id;

    private String content;

    private String schedule;

    private String scheduleTime;

    private String receiver;

    private String sendMsg;

    /** 发送类型0.人员发送1.机构发送2.组发送 */
    private String sendType="0";

    private String groupId;

    private String groupName;

    private String deptId;

    private String deptName;

    private Date createTime;

    private Date updateTime;

    private List<String> receiverList;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public List<String> getReceiverList() {
        if(StringUtils.isNotBlank(this.receiver)){
            return Arrays.asList(receiver.split(","));
        }
        return receiverList;
    }

    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}