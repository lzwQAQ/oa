package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;

/**
 * 驾驶员申请实体
 *
 * @author administrator
 */
public class Driver extends BaseEntity {

    private String id;

    private String name;

    private String phone;

    private String sex;

    /**
     * 驾龄
     */
    private Integer drivingYear;

    public Driver(String id) {
        this.id = id;
    }

    public Driver() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getDrivingYear() {
        return drivingYear;
    }

    public void setDrivingYear(Integer drivingYear) {
        this.drivingYear = drivingYear;
    }
}