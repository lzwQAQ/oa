package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.TreeEntity;

import org.hibernate.validator.constraints.Length;

/**
 * 部门实体
 *
 * @author administrator
 */
public class Dept extends TreeEntity<Dept> {

    /**
     * 部门代码
     */
    @Length(max = 50, message = "部门代码长度不能大于50")
    private String code;

    /**
     * 部门名称
     */
    @Length(max = 50, message = "部门名称长度不能大于50")
    private String name;

    /**
     * 部门简称
     */
    @Length(max = 20, message = "部门简称长度不能大于20")
    private String simpleName;

    /**
     * 所属机构
     */
    private Org org;

    /**
     * 负责人
     */
    @Length(max = 8, message = "负责人长度不能大于8")
    private String master;

    /**
     * 电话
     */
    @Length(max = 11, message = "电话长度不能大于11")
    private String phone;

    /**
     * 传真
     */
    @Length(max = 15, message = "传真长度不能大于15")
    private String fax;

    /**
     * 邮箱
     */
    @Length(max = 20, message = "邮箱长度不能大于20")
    private String email;

    /**
     * 序号
     */
    @Length(max = 11, message = "序号长度不能大于11")
    private Integer sort;


    public Dept(String id) {
        this.id = id;
    }

    public Dept() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentName() {
        return getParent() != null ? getParent().getName() : null;
    }

    @Override
    public Dept getParent() {
        return parent;
    }

    @Override
    public void setParent(Dept parent) {
        this.parent = parent;
    }
}