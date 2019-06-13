package com.kuyuner.workflow.api.bean;

import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 任务与业务关联实体
 *
 * @author administrator
 */
public class TaskBusinessKey extends BaseEntity {

	/**
     * 业务ID
     */     
	@NotNull(message = "业务ID不能为空")
	@Length(max = 32, message = "业务ID长度不能大于32")
	private String businessId;

	/**
     * 业务日志ID
     */     
	@NotNull(message = "业务日志ID不能为空")
	@Length(max = 32, message = "业务日志ID长度不能大于32")
	private String businessLogId;

	/**
     * 流程实例ID
     */     
	@NotNull(message = "流程实例ID不能为空")
	@Length(max = 32, message = "流程实例ID长度不能大于32")
	private String procInstId;

	/**
     * 任务ID
     */     
	@NotNull(message = "任务ID不能为空")
	@Length(max = 32, message = "任务ID长度不能大于32")
	private String taskId;



    public TaskBusinessKey(String id) {
        this.id = id;
    }

    public TaskBusinessKey() {
        super();
    }

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessLogId() {
		return businessLogId;
	}

	public void setBusinessLogId(String businessLogId) {
		this.businessLogId = businessLogId;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



}