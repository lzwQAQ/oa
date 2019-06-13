package com.kuyuner.bg.msg.entity;

import com.kuyuner.common.persistence.BaseEntity;

import java.util.Date;

/**
 * 收文实体
 *
 * @author administrator
 */
public class MessageSend extends BaseEntity {

    private String id;
    //收信人
    private String receiver;
    //发送状态0。失败1.成功
    private String status;

    private String seqId;

    private String msgId;

    private Date createTime;

    private Date updateTime;

    private String sendStatusDsc = "";

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSendStatusDsc() {
        return sendStatusDsc;
    }

    public void setSendStatusDsc(String sendStatusDsc) {
        this.sendStatusDsc = sendStatusDsc;
    }
}