package com.kuyuner.workflow.manage.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuyuner.common.persistence.BaseEntity;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 任务日志实体
 *
 * @author administrator
 */
public class TaskLog extends BaseEntity {

	/**
     * 任务名称
     */     
	@NotNull(message = "任务名称不能为空")
	@Length(max = 255, message = "任务名称长度不能大于255")
	private String taskName;

	/**
     * 处理人
     */     
	@NotNull(message = "处理人不能为空")
	@Length(max = 32, message = "处理人长度不能大于32")
	private String assignee;

	/**
     * 审批意见
     */     
	@NotNull(message = "审批意见不能为空")
	@Length(max = 64, message = "审批意见长度不能大于64")
	private String approvalOpinion;

	/**
     * 任务ID
     */     
	@NotNull(message = "任务ID不能为空")
	@Length(max = 32, message = "任务ID长度不能大于32")
	private String taskId;

	/**
     * 事件名称
     */     
	@NotNull(message = "事件名称不能为空")
	@Length(max = 32, message = "事件名称长度不能大于32")
	private String eventName;

	/**
     * 审批时间
     */     
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "审批时间不能为空")
	private Date approvalDate;

	/**
     * 流程实例ID
     */     
	@NotNull(message = "流程实例ID不能为空")
	@Length(max = 32, message = "流程实例ID长度不能大于32")
	private String proInstId;



    public TaskLog(String id) {
        this.id = id;
    }

    public TaskLog() {
        super();
    }

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getApprovalOpinion() {
		return approvalOpinion;
	}

	public void setApprovalOpinion(String approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getProInstId() {
		return proInstId;
	}

	public void setProInstId(String proInstId) {
		this.proInstId = proInstId;
	}



}