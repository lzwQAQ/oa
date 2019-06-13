package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 字典类型实体
 *
 * @author administrator
 */
public class DictType extends BaseEntity {

    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    @Length(max = 50, message = "字典名称长度不能大于50")
    private String dictName;

    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空")
    @Length(max = 20, message = "字典类型长度不能大于20")
    private String type;

    /**
     * 系统字典（1是 0否）
     */
    @NotNull(message = "系统字典不能为空")
    @Length(max = 1, message = "系统字典长度不能大于1")
    private String isSysData;

    /**
     * 序号
     */
    @Length(max = 11, message = "序号长度不能大于11")
    private Integer sort;

    public DictType(String id) {
        this.id = id;
    }

    public DictType() {
        super();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsSysData() {
        return isSysData;
    }

    public void setIsSysData(String isSysData) {
        this.isSysData = isSysData;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}