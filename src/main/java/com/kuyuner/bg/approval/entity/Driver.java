package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.sys.entity.User;

/**
 * 车辆申请实体
 *
 * @author administrator
 */
public class Driver extends BaseEntity {

    private User user;

    /**
     * 驾龄
     */
    private Integer drivingYear;

    public Driver(String id) {
        this.id = id;
    }

    public Driver() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDrivingYear() {
        return drivingYear;
    }

    public void setDrivingYear(Integer drivingYear) {
        this.drivingYear = drivingYear;
    }
}