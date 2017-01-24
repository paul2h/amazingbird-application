package com.wavegis.model.esav;

/** 參數若只要單一的stid都可以用此model傳參數 */
public class ReadDataByStidParam {
	private AuthAction userAction;
	private String sTId;

	public AuthAction getUserAction() {
		return userAction;
	}

	public void setUserAction(AuthAction userAction) {
		this.userAction = userAction;
	}

	public String getsTId() {
		return sTId;
	}

	public void setsTId(String sTId) {
		this.sTId = sTId;
	}

}
