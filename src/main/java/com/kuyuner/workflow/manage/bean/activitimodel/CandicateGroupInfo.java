package com.kuyuner.workflow.manage.bean.activitimodel;

import java.io.Serializable;

/**
 * 用户接受前端传输过来的json数据
 * 
 * @author tangzj
 *
 */
public class CandicateGroupInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] roles = {};
	private String[] depts = {};
	private String[] orgs = {};

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String[] getDepts() {
		return depts;
	}

	public void setDepts(String[] depts) {
		this.depts = depts;
	}

	public String[] getOrgs() {
		return orgs;
	}

	public void setOrgs(String[] orgs) {
		this.orgs = orgs;
	}

}
