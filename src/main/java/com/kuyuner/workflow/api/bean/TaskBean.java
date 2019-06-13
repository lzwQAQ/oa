package com.kuyuner.workflow.api.bean;

import java.util.List;

/**
 * 任务处理结果信息，用于保存前端传递的流程相关信息
 * 
 * @author tangzj
 *
 */
public class TaskBean {
	private String sequenceFlowName;
	private String startSequenceFlowName;
	private String taskId;
	private String processDefinitionId;
	private List<String> users;

	public String getSequenceFlowName() {
		return sequenceFlowName;
	}

	public void setSequenceFlowName(String sequenceFlowName) {
		this.sequenceFlowName = sequenceFlowName;
	}

	public String getStartSequenceFlowName() {
		return startSequenceFlowName;
	}

	public void setStartSequenceFlowName(String startSequenceFlowName) {
		this.startSequenceFlowName = startSequenceFlowName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

}
