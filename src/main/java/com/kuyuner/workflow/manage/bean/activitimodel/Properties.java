package com.kuyuner.workflow.manage.bean.activitimodel;

public class Properties {
	private String process_id = "";
	private String name = "";
	private String documentation = "";
	private String process_author = "";
	private String process_version = "";
	private String process_namespace = "";
	private ExecutionListeners executionlisteners = new ExecutionListeners();

	public String getProcess_id() {
		return process_id;
	}

	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public String getProcess_author() {
		return process_author;
	}

	public void setProcess_author(String process_author) {
		this.process_author = process_author;
	}

	public String getProcess_version() {
		return process_version;
	}

	public void setProcess_version(String process_version) {
		this.process_version = process_version;
	}

	public String getProcess_namespace() {
		return process_namespace;
	}

	public void setProcess_namespace(String process_namespace) {
		this.process_namespace = process_namespace;
	}

	public ExecutionListeners getExecutionlisteners() {
		return executionlisteners;
	}

	public void setExecutionlisteners(ExecutionListeners executionlisteners) {
		this.executionlisteners = executionlisteners;
	}

}
