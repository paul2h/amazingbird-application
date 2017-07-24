package com.wavegis.model.message;

public class Message {

	private String from_email;
	private String user_name;
	private String photoUrl;
	private String text;
	private String timestamp;
	private boolean hasSend;
	
	public Message() {
	}
	
	public Message(String from_email, String name, String photoUrl, String text, String timestamp) {
		super();
		this.from_email = from_email;
		this.user_name = name;
		this.photoUrl = photoUrl;
		this.text = text;
		this.timestamp = timestamp;
	}

	public String getFrom_email() {
		return from_email;
	}
	public void setFrom_email(String from_email) {
		this.from_email = from_email;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isHasSend() {
		return hasSend;
	}

	public void setHasSend(boolean hasSend) {
		this.hasSend = hasSend;
	}

}
