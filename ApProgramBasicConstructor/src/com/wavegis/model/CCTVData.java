package com.wavegis.model;

public class CCTVData {

	private String stname;
	private String URL;
	private boolean need_login = false;
	private String account;
	private String password;
	private String savePath;

	public String getStname() {
		return stname;
	}

	public void setStname(String stname) {
		this.stname = stname;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isNeed_login() {
		return need_login;
	}

	public void setNeed_login(boolean need_login) {
		this.need_login = need_login;
	}

}
