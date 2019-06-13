package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;

/**
 * 车辆申请实体
 *
 * @author administrator
 */
public class Car extends BaseEntity {

    /**
     * 车牌号
     */
    private String carNo;

    /**
     * 车辆颜色
     */
    private String color;

    /**
     * 品牌型号
     */
    private String brand;

    /**
     * 座位数
     */
    private String seatNum;

    public Car(String id) {
        this.id = id;
    }

    public Car() {
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }
}