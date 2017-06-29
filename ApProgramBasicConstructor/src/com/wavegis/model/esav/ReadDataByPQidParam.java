package com.wavegis.model.esav;

/** 依照PQID取得資料所需的參數 */
public class ReadDataByPQidParam {
	private AuthAction userAction;
	private String pQId;

	public AuthAction getUserAction() {
		return userAction;
	}

	public void setUserAction(AuthAction userAction) {
		this.userAction = userAction;
	}

	public String getpQId() {
		return pQId;
	}

	public void setpQId(String pQId) {
		this.pQId = pQId;
	}
}
