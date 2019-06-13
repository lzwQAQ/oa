package com.kuyuner.workflow.manage.bean.activitimodel;

public class ExecutionListener {
	private String event = "";
	private String implementation = "";
	private String className = "";
	private String expression = "";
	private String delegateExpression = "";

	public ExecutionListener() {
	}

	public ExecutionListener(String event, String implementation, String className) {
		this.event = event;
		this.implementation = implementation;
		this.className = className;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getDelegateExpression() {
		return delegateExpression;
	}

	public void setDelegateExpression(String delegateExpression) {
		this.delegateExpression = delegateExpression;
	}

}
