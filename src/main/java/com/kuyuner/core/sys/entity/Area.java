package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.TreeEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
/**
 * 区划实体
 *
 * @author administrator
 */
public class Area extends TreeEntity<Area> {

	/**
     * 区划编码
     */     
	@NotNull(message = "区划编码不能为空")
	@Length(max = 50, message = "区划编码长度不能大于50")
	private String code;

	/**
     * 区划名称
     */     
	@NotNull(message = "区划名称不能为空")
	@Length(max = 50, message = "区划名称长度不能大于50")
	private String name;

	/**
     * 序号
     */     
	@Length(max = 11, message = "序号长度不能大于11")
	private Integer sort;



    public Area(String id) {
        this.id = id;
    }

    public Area() {
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}



    @Override
    public Area getParent() {
        return parent;
    }

    @Override
    public void setParent(Area parent) {
        this.parent = parent;
    }
}