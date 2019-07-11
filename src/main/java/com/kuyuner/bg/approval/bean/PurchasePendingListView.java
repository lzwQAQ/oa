package com.kuyuner.bg.approval.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import java.util.Date;
import java.util.List;

/**
 * @author administrator
 */
public class PurchasePendingListView extends BaseEntity {
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 请假类型
     */
    @DictType("LEAVE_TYPE")
    private String type;

    /**
     * 当前环节
     */
    private String taskName;

    private String name;

    /**
     * 申请人
     */
    private String sender;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String sendDeptName;

    private String reason;
    private String approvalResult;
    private String totalPrice;

    private List<PurchaseGoods> goods;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendDeptName() {
        return sendDeptName;
    }

    public void setSendDeptName(String sendDeptName) {
        this.sendDeptName = sendDeptName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<PurchaseGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<PurchaseGoods> goods) {
        this.goods = goods;
    }
}
