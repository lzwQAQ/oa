package com.kuyuner.workflow.manage.bean;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 任务权限实体
 *
 * @author administrator
 */
public class TaskAuth extends BaseEntity {

	/**
     * 角色ID、部门ID、机构ID
     */     
	@NotNull(message = "角色ID、部门ID、机构ID不能为空")
	@Length(max = 32, message = "角色ID、部门ID、机构ID长度不能大于32")
	private String objectId;

	/**
     * 有三种类型：角色
     */     
	@NotNull(message = "有三种类型：角色不能为空")
	@Length(max = 32, message = "有三种类型：角色长度不能大于32")
	private String objectType;

	/**
     * 任务key
     */     
	@NotNull(message = "任务key不能为空")
	@Length(max = 50, message = "任务key长度不能大于50")
	private String taskKey;

	/**
     * 模型key，即流程定义key
     */     
	@NotNull(message = "模型key，即流程定义key不能为空")
	@Length(max = 255, message = "模型key，即流程定义key长度不能大于255")
	private String modelKey;



    public TaskAuth(String id) {
        this.id = id;
    }

    public TaskAuth() {
        super();
    }

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getModelKey() {
		return modelKey;
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}



}