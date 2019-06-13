package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.TreeEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 菜单实体
 *
 * @author administrator
 */
public class Menu extends TreeEntity<Menu> {

    /**
     * 菜单名称
     */
    @NotNull(message = "菜单名称不能为空")
    @Length(max = 50, message = "菜单名称长度不能大于50")
    private String name;

    /**
     * 菜单链接
     */
    @Length(max = 50, message = "菜单链接长度不能大于50")
    private String href;

    /**
     * 图标
     */
    @Length(max = 100, message = "图标长度不能大于100")
    private String menuIcon;

    /**
     * 菜单状态
     */
    @NotNull(message = "菜单状态不能为空")
    @Length(max = 1, message = "菜单状态长度不能大于1")
    private String menuStatus;

    /**
     * 权限标识
     */
    @Length(max = 100, message = "权限标识长度不能大于100")
    private String permissions;

    /**
     * 状态
     */
    @Length(max = 1, message = "状态长度不能大于1")
    private String type;

    /**
     * 序号
     */
    @Length(max = 11, message = "序号长度不能大于11")
    private Integer sort;


    public Menu(String id) {
        this.id = id;
    }

    public Menu() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Override
    public Menu getParent() {
        return parent;
    }

    @Override
    public void setParent(Menu parent) {
        this.parent = parent;
    }
}