package com.wavegis.model;

public class MailData {

	private String mail_subject;
	private String text_content;
	private String attach_file_path;
	private String targetMails;
	
	public MailData(String mail_subject, String text_content, String attach_file_path, String targetMails) {
		super();
		this.mail_subject = mail_subject;
		this.text_content = text_content;
		this.attach_file_path = attach_file_path;
		this.targetMails = targetMails;
	}

	public String getAttach_file_path() {
		return attach_file_path;
	}

	public void setAttach_file_path(String attach_file_path) {
		this.attach_file_path = attach_file_path;
	}

	public String getTargetMails() {
		return targetMails;
	}

	public void setTargetMails(String targetMails) {
		this.targetMails = targetMails;
	}

	public String[] getTargetMailArray() {
		return targetMails.split(",");
	}

	public String getMail_subject() {
		return mail_subject;
	}

	public void setMail_subject(String mail_subject) {
		this.mail_subject = mail_subject;
	}

	public String getText_content() {
		return text_content;
	}

	public void setText_content(String text_content) {
		this.text_content = text_content;
	}
}
