package com.wavegis.model.esav;

/** 安研範例實作 */
public class ResponseResult {
	private short Code;
	private String Message;

	public short getCode() {
		return Code;
	}

	public void setCode(short code) {
		Code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
