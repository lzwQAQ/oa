package com.kuyuner.workflow.manage.bean.activitimodel;

public class ActivitiModel {
	private String resourceId = "";
	private Properties properties = new Properties();
	private Stencilset stencilset = new Stencilset();

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Stencilset getStencilset() {
		return stencilset;
	}

	public void setStencilset(Stencilset stencilset) {
		this.stencilset = stencilset;
	}

}
