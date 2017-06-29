package com.wavegis.model;

public class WebMonitorFocusData {

	private int project_id;
	private String project_name;
	private String web_url;
	private String group_name;
	private String status_name;
	private boolean has_url;
	private String report_users;
	private String report_mails;
	private int group_id;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public String getReport_users() {
		return report_users;
	}

	public void setReport_users(String report_users) {
		this.report_users = report_users;
	}

	public String getReport_mails() {
		return report_mails;
	}

	public void setReport_mails(String report_mails) {
		this.report_mails = report_mails;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getWeb_url() {
		return web_url;
	}

	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public boolean isHas_url() {
		return has_url;
	}

	public void setHas_url(boolean has_url) {
		this.has_url = has_url;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

}
