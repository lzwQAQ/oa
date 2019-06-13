package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.TreeEntity;

/**
 * 机构
 *
 * @author administrator
 */
public class Org extends TreeEntity<Org> {

    /**
     * 机构编码
     */
    private String code;
    /**
     * 机构全称
     */
    private String name;
    /**
     * 机构简称
     */
    private String simpleName;
    /**
     * 地址
     */
    private String address;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 负责人
     */
    private String master;
    /**
     * 办公电话
     */
    private String phone;
    /**
     * 传真
     */
    private String fax;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 排序号
     */
    private Integer sort;

    public Org(String id) {
        this.id = id;
    }

    public Org() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    @Override
    public Org getParent() {
        return parent;
    }

    @Override
    public void setParent(Org parent) {
        this.parent = parent;
    }
}