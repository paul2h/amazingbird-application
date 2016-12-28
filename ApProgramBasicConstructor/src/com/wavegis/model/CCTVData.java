package com.wavegis.model;

public class CCTVData {
	private String stid;
	private String stname;
	private String ip;
	private String url_suffix;
	private String account;
	private String password;
	private String savePath;
	private String filename;

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
	
	public String getIp(){
		return ip;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getUrl_suffix(){
		return url_suffix;
	}

	public void setUrl_suffix(String url_suffix){
		this.url_suffix = url_suffix;
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
}
