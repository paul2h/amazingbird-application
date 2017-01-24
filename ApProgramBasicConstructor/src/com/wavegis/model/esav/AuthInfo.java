package com.wavegis.model.esav;


/** 安研範例實作 */
public class AuthInfo {
	private String Username;
	private String Password;
	private String Format = "json";

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}
}
