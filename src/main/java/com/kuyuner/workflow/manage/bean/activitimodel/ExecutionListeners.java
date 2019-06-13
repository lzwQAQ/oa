package com.kuyuner.workflow.manage.bean.activitimodel;

import java.util.ArrayList;
import java.util.List;

public class ExecutionListeners {
	private List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();

	public List<ExecutionListener> getExecutionListeners() {
		return executionListeners;
	}

	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}

}
