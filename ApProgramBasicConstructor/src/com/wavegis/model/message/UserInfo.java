package com.wavegis.model.message;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {
	
	private String device_OS;
	private String email;
	private String fcm_token;
	private String photoUrl;
	private String user_name;
	
	public UserInfo() {
		
	}
	
	public UserInfo(String device_OS, String email, String fcm_token, String photoURL, String user_name) {
		super();
		this.device_OS = device_OS;
		this.email = email;
		this.fcm_token = fcm_token;
		this.photoUrl = photoURL;
		this.user_name = user_name;
	}

	public String getDevice_OS() {
		return device_OS;
	}

	public void setDevice_OS(String device_OS) {
		this.device_OS = device_OS;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFcm_token() {
		return fcm_token;
	}

	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}

	public String getPhotoURL() {
		return photoUrl;
	}

	public void setPhotoURL(String photoURL) {
		this.photoUrl = photoURL;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
