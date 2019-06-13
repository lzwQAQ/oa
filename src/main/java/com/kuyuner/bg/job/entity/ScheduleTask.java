package com.kuyuner.bg.job.entity;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
/**
 * 定时任务实体
 *
 * @author administrator
 */
public class ScheduleTask extends BaseEntity {

	/**
     * 任务名称
     */     
	@Length(max = 20, message = "任务名称长度不能大于20")
	private String taskName;

	/**
     * 任务类型
     */     
	@NotNull(message = "任务类型不能为空")
	@Length(max = 20, message = "任务类型长度不能大于20")
	private String taskType;

	/**
     * 类名
     */     
	@NotNull(message = "类名不能为空")
	@Length(max = 32, message = "类名长度不能大于32")
	private String className;

	/**
     * 方法名
     */     
	@NotNull(message = "方法名不能为空")
	@Length(max = 32, message = "方法名长度不能大于32")
	private String methodName;

	/**
     * cron
     */     
	@NotNull(message = "cron不能为空")
	@Length(max = 50, message = "cron长度不能大于50")
	private String cron;

	/**
     * 数据
     */     
	@Length(max = 255, message = "数据长度不能大于255")
	private String data;



    public ScheduleTask(String id) {
        this.id = id;
    }

    public ScheduleTask() {
        super();
    }

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}



}