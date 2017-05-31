package com.wavegis.model;

public class FocusData {

	private int _id;
	private String project_name;
	private String web_url;
	private int group_id;
	private boolean has_url;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
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
	
}
