package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 角色实体
 *
 * @author administrator
 */
public class Role extends BaseEntity {

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    @Length(max = 20, message = "角色名称长度不能大于20")
    private String name;

    /**
     * 角色代码
     */
    @NotNull(message = "角色代码不能为空")
    @Length(max = 100, message = "角色代码长度不能大于100")
    private String code;

    /**
     * 系统内置
     */
    @NotNull(message = "系统内置不能为空")
    @Length(max = 1, message = "系统内置长度不能大于1")
    private String isSys;

    /**
     * 所属机构
     */
    @Length(max = 32, message = "所属机构长度不能大于32")
    private Org org;

    /**
     * 备注
     */
    @Length(max = 50, message = "备注长度不能大于50")
    private String mark;


    public Role(String id) {
        this.id = id;
    }

    public Role() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


}