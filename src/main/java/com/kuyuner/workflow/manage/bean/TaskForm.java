package com.kuyuner.workflow.manage.bean;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 任务表单实体
 *
 * @author administrator
 */
public class TaskForm extends BaseEntity {

	/**
     * 表单名称
     */     
	@Length(max = 20, message = "表单名称长度不能大于20")
	private String formName;

	/**
     * 表单路径
     */     
	@Length(max = 255, message = "表单路径长度不能大于255")
	private String formPath;

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



    public TaskForm(String id) {
        this.id = id;
    }

    public TaskForm() {
        super();
    }

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormPath() {
		return formPath;
	}

	public void setFormPath(String formPath) {
		this.formPath = formPath;
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