package com.kuyuner.workflow.manage.bean.activitimodel;

public class Stencilset {
	private String namespace = "";

	public Stencilset() {
	}

	public Stencilset(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
