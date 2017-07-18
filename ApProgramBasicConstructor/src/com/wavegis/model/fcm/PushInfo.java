package com.wavegis.model.fcm;

import java.util.List;
import java.util.Map;

import org.pixsee.fcm.Message;
import org.pixsee.fcm.Notification;

public class PushInfo {
	
	private String title;
	private String body;
	private String deviceOS;
	private List<String> tokens;
	private Map<String, Object> datas;
	
	public PushInfo() {
		
	}
	
	public PushInfo(String title, String body, String deviceOS, List<String> tokens, Map<String, Object> datas) {
		super();
		this.title = title;
		this.body = body;
		this.deviceOS = deviceOS;
		this.tokens = tokens;
		this.datas = datas;
	}
	
	public Message genPushMessage() {
		Notification notification = deviceOS.equals("iOS") ? 
										new Notification(title, body) : 
										null;
		return new Message.MessageBuilder()
					      .addRegistrationToken(tokens) 
					      .addData(datas)
					      .contentAvailable(true)
					      .notification(notification)
					      .build();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDeviceOS() {
		return deviceOS;
	}
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}
	public List<String> getTokens() {
		return tokens;
	}
	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
	public Map<String, Object> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	
}
