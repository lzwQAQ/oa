package com.kuyuner.core.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuyuner.common.security.Principal;
import com.kuyuner.core.common.dict.DictType;
import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 用户实体
 *
 * @author administrator
 */
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Length(max = 10, message = "用户名长度不能大于10")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Length(max = 32, message = "密码长度不能大于32")
    @JsonIgnore
    private String password;

    /**
     * 姓名
     */
    @Length(max = 10, message = "姓名长度不能大于10")
    private String name;


    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginDate;

    /**
     * 登录错误次数
     */
    @Length(max = 11, message = "登录错误次数长度不能大于11")
    private Integer loginErrorCount;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @Length(max = 5, message = "状态长度不能大于5")
    @DictType("USER_STATE")
    private String state;

    /**
     * 用户头像
     */
    @Length(max = 255, message = "用户头像长度不能大于255")
    private String photo;

    /**
     * 性别
     */
    @Length(max = 1, message = "性别长度不能大于1")
    private String sex;

    /**
     * 手机号
     */
    @Length(max = 11, message = "手机号长度不能大于255")
    private String phone;

    /**
     * 邮箱
     */
    @Length(max = 50, message = "邮箱长度不能大于255")
    private String email;

    /**
     * 部门
     */
    private Dept dept;

    /**
     * 家庭住址
     */
    @Length(max = 40, message = "家庭住址长度不能大于40")
    private String homePlace;

    /**
     * 常住地址
     */
    @Length(max = 40, message = "常住地址长度不能大于40")
    private String usualPlace;

    /**
     * 是否结婚
     */
    @Length(max = 1, message = "是否结婚长度不能大于1")
    private String isMarry;

    /**
     * 毕业院校
     */
    @Length(max = 30, message = "毕业院校长度不能大于30")
    private String school;

    /**
     * 是否党员
     */
    @Length(max = 1, message = "是否党员长度不能大于1")
    private String isParty;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 入职时间
     */
    private String entryDate;

    /**
     * 职位
     */
    @Length(max = 20, message = "职位长度不能大于20")
    private String position;

    /**
     * 紧急联系人
     */
    @Length(max = 8, message = "紧急联系人长度不能大于8")
    private String urgentContacts;


    public User(Principal principal) {
        super();
        username = principal.getUsername();
        name = principal.getName();
        id = principal.getId();
    }

    public User(String id) {
        this.id = id;
    }

    public User() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getLoginErrorCount() {
        return loginErrorCount;
    }

    public void setLoginErrorCount(Integer loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public String getHomePlace() {
        return homePlace;
    }

    public void setHomePlace(String homePlace) {
        this.homePlace = homePlace;
    }

    public String getUsualPlace() {
        return usualPlace;
    }

    public void setUsualPlace(String usualPlace) {
        this.usualPlace = usualPlace;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUrgentContacts() {
        return urgentContacts;
    }

    public void setUrgentContacts(String urgentContacts) {
        this.urgentContacts = urgentContacts;
    }
}