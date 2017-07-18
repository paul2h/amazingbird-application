package com.wavegis.model.water;

import java.sql.Timestamp;

public class WaterAlertData {

	private String stid;
	private String stnm;
	private Timestamp datatime;
	private double waterlv;
	private double alert_value;
	private String message;
	private String phones;
	private boolean hasSend = false;

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public String getStnm() {
		return stnm;
	}

	public void setStnm(String stnm) {
		this.stnm = stnm;
	}

	public Timestamp getDatatime() {
		return datatime;
	}

	public void setDatatime(Timestamp datatime) {
		this.datatime = datatime;
	}

	public double getWaterlv() {
		return waterlv;
	}

	public void setWaterlv(double waterlv) {
		this.waterlv = waterlv;
	}

	public double getAlert_value() {
		return alert_value;
	}

	public void setAlert_value(double alert_value) {
		this.alert_value = alert_value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public boolean HasSend() {
		return hasSend;
	}

	public void setHasSend(boolean hasSend) {
		this.hasSend = hasSend;
	}
}
