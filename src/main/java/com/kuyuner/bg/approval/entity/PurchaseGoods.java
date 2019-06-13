package com.kuyuner.bg.approval.entity;

import com.kuyuner.common.persistence.BaseEntity;
import com.kuyuner.core.common.dict.DictType;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 采购清单实体
 *
 * @author administrator
 */
public class PurchaseGoods extends BaseEntity {

    /**
     * 采购单ID
     */
    @Length(max = 32, message = "采购单ID长度不能大于32")
    private String purchaseId;

    /**
     * 物品名称
     */
    @NotNull(message = "物品名称不能为空")
    @Length(max = 50, message = "物品名称长度不能大于50")
    private String name;

    /**
     * 型号
     */
    @NotNull(message = "型号不能为空")
    @Length(max = 50, message = "型号长度不能大于50")
    private String model;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    private Double price;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Length(max = 11, message = "数量长度不能大于11")
    private Integer num;

    /**
     * 单位
     */
    @NotNull(message = "单位不能为空")
    @Length(max = 2, message = "单位长度不能大于2")
    @DictType("GOODS_UNIT")
    private String unit;

    /**
     * 总价
     */
    private String totalPrice;

    public PurchaseGoods(String id) {
        this.id = id;
    }

    public PurchaseGoods() {
        super();
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}