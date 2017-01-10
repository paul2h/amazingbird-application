package com.wavegis.model.esav;

/**
 * 由STID或PQID查詢後 回傳的資料model
 * */
public class DataByID {
	private Data Data;
	private String PQId;

	public Data getData() {
		return Data;
	}

	public void setData(Data data) {
		Data = data;
	}

	public String getPQId() {
		return PQId;
	}

	public void setPQId(String pQId) {
		PQId = pQId;
	}
}
