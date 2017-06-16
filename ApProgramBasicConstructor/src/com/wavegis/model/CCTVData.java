package com.wavegis.model;

public class CCTVData {
	private String stid;
	private String stname;
	private String account;
	private String password;
	private String savePath;
	private String filename;
	private boolean need_login = false;
	private String URL;

	public String getStid(){
		return stid;
	}

	public void setStid(String stid){
		this.stid = stid;
	}
	
	public String getStname(){
		return stname;
	}

	public void setStname(String stname){
		this.stname = stname;
	}
	
	public String getAccount(){
		return account;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getSavePath(){
		return savePath;
	}

	public void setSavePath(String savePath){
		this.savePath = savePath;
	}

	public String getFilename(){
		return filename;
	}

	public void setFilename(String filename){
		this.filename = filename;
	}

	public boolean isNeed_login() {
		return need_login;
	}

	public void setNeed_login(boolean need_login) {
		this.need_login = need_login;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
}
