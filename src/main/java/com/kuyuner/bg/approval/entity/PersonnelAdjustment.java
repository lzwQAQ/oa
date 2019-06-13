package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 人事调度实体
 *
 * @author administrator
 */
public class PersonnelAdjustment extends BaseEntity {

    /**
     * 当前环节
     */
    @Length(max = 20, message = "当前环节长度不能大于20")
    private String taskName;

    /**
     * 审批结果
     */
    private String approvalResult;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Length(max = 1, message = "性别长度不能大于1")
    private String sex;

    /**
     * 是否结婚
     */
    @NotNull(message = "是否结婚不能为空")
    @Length(max = 1, message = "是否结婚长度不能大于1")
    private String isMarry;

    /**
     * 毕业院校
     */
    @NotNull(message = "毕业院校不能为空")
    @Length(max = 30, message = "毕业院校长度不能大于30")
    private String school;

    /**
     * 是否党员
     */
    @NotNull(message = "是否党员不能为空")
    @Length(max = 1, message = "是否党员长度不能大于1")
    private String isParty;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @Length(max = 10, message = "出生日期长度不能大于10")
    private String birthday;

    /**
     * 入职时间
     */
    @NotNull(message = "入职时间不能为空")
    @Length(max = 10, message = "入职时间长度不能大于10")
    private String entryDate;

    /**
     * 户籍所在地
     */
    @NotNull(message = "户籍所在地不能为空")
    @Length(max = 40, message = "户籍所在地长度不能大于40")
    private String homePlace;

    /**
     * 现任职
     */
    @NotNull(message = "现任职不能为空")
    @Length(max = 5, message = "现任职长度不能大于5")
    @DictType("PEOPLE_POSITION")
    private String oldPosition;

    /**
     * 调往部门
     */
    @NotNull(message = "调往部门不能为空")
    @Length(max = 32, message = "调往部门长度不能大于32")
    private String newDeptId;

    /**
     * 新任职位
     */
    @Length(max = 30, message = "新任职位长度不能大于30")
    private String newPosition;

    private String senderId;

    private String senderName;

    private String senderDeptName;

    private String newDeptName;

    public PersonnelAdjustment(String id) {
        this.id = id;
    }

    public PersonnelAdjustment() {
        super();
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsMarry() {
        return isMarry;
    }

    public void setIsMarry(String isMarry) {
        this.isMarry = isMarry;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIsParty() {
        return isParty;
    }

    public void setIsParty(String isParty) {
        this.isParty = isParty;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getHomePlace() {
        return homePlace;
    }

    public void setHomePlace(String homePlace) {
        this.homePlace = homePlace;
    }

    public String getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getNewDeptId() {
        return newDeptId;
    }

    public void setNewDeptId(String newDeptId) {
        this.newDeptId = newDeptId;
    }

    public String getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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

    public String getNewDeptName() {
        return newDeptName;
    }

    public void setNewDeptName(String newDeptName) {
        this.newDeptName = newDeptName;
    }
}