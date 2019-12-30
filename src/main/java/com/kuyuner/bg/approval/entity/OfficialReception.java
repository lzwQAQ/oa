package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 公务接待申请实体
 *
 * @author administrator
 */
public class OfficialReception extends BaseEntity {

    /**
     * 来宾数
     */
    @NotNull(message = "来宾数不能为空")
    @Length(max = 4, message = "来宾数长度不能大于4")
    private Integer guest;

    /**
     * 陪客数
     */
    @NotNull(message = "陪客数不能为空")
    @Length(max = 4, message = "陪客数长度不能大于4")
    private Integer accompany;

    /**
     * 吃饭标准
     */
    @NotNull(message = "吃饭标准不能为空")
    @Length(max = 100, message = "吃饭标准长度不能大于100")
    private String mealStandards;

    /**
     * 饭店名称
     */
    @NotNull(message = "饭店名称不能为空")
    @Length(max = 100, message = "饭店名称长度不能大于100")
    private String hotelName;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @Length(max = 20, message = "开始时间长度不能大于20")
    private String startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @Length(max = 20, message = "结束时间长度不能大于20")
    private String endTime;

    /**
     * 申请事由
     */
    @NotNull(message = "申请事由不能为空")
    @Length(max = 120, message = "申请事由长度不能大于120")
    private String reason;

    /**
     * 当前环节
     */
    @Length(max = 50, message = "当前环节长度不能大于50")
    private String taskName;

    /**
     * 申请人ID
     */
    private String senderId;

    /**
     * 审批结果
     */
    private String approvalResult;

    /**
     * 发送人
     */
    private String senderName;

    /**
     * 所属部门
     */
    private String senderDeptName;

    public OfficialReception(String id) {
        this.id = id;
    }

    public OfficialReception() {
        super();
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDeptName() {
        return senderDeptName;
    }

    public void setSenderDeptName(String senderDeptName) {
        this.senderDeptName = senderDeptName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Integer getGuest() {
        return guest;
    }

    public void setGuest(Integer guest) {
        this.guest = guest;
    }

    public Integer getAccompany() {
        return accompany;
    }

    public void setAccompany(Integer accompany) {
        this.accompany = accompany;
    }

    public String getMealStandards() {
        return mealStandards;
    }

    public void setMealStandards(String mealStandards) {
        this.mealStandards = mealStandards;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}