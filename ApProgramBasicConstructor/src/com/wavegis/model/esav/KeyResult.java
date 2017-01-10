package com.wavegis.model.esav;


/** 安研範例實作 */
public class KeyResult{

	private String Key;
	private ResponseResult Response;

	public ResponseResult getResponse() {
		return Response;
	}

	public void setResponse(ResponseResult response) {
		Response = response;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}
}
