package com.kuyuner.core.sys.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
/**
 * 字典数据实体
 *
 * @author administrator
 */
public class DictData extends BaseEntity {

	/**
     * 字典代码
     */     
	@NotNull(message = "字典代码不能为空")
	@Length(max = 5, message = "字典代码长度不能大于5")
	private String dictCode;

	/**
     * 字典值
     */     
	@NotNull(message = "字典值不能为空")
	@Length(max = 50, message = "字典值长度不能大于50")
	private String dictValue;

	/**
     * 字典类型
     */     
	@NotNull(message = "字典类型不能为空")
	@Length(max = 20, message = "字典类型长度不能大于20")
	private String dictType;

	/**
     * 序号
     */     
	@Length(max = 11, message = "序号长度不能大于11")
	private Integer sort;

    public DictData(String id) {
        this.id = id;
    }

    public DictData() {
        super();
    }

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}



}