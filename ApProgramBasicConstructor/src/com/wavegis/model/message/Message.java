package com.wavegis.model.message;

public class Message {

	private String from_email;
	private String name;
	private String photoUrl;
	private String text;
	private long timestamp;
	
	public Message() {
	}
	
	public Message(String from_email, String name, String photoUrl, String text, long timestamp) {
		super();
		this.from_email = from_email;
		this.name = name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
