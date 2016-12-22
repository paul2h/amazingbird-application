package com.wavegis.model;

import java.sql.Timestamp;

public class RainData {
	private String stno;// 雨量站編號
	private String stnm;// 站名
	private Timestamp lasttime;
	private String time;
	private double min_10;
	private double rain;
	private double hour_3;
	private double hour_6;
	private double hour_12;
	private double hour_24;

	/** 物件名稱 */
	public String getClass_name() {
		return "rain_station";
	}

	public String getStno() {
		return stno;
	}

	public void setStno(String stno) {
		this.stno = stno;
	}

	public String getStnm() {
		return stnm;
	}

	public void setStnm(String stnm) {
		this.stnm = stnm;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getRain() {
		return rain;
	}

	public void setRain(double rain) {
		this.rain = rain;
	}

	public double getHour_3() {
		return hour_3;
	}

	public void setHour_3(double hour_3) {
		this.hour_3 = hour_3;
	}

	public double getHour_6() {
		return hour_6;
	}

	public void setHour_6(double hour_6) {
		this.hour_6 = hour_6;
	}

	public double getHour_12() {
		return hour_12;
	}

	public void setHour_12(double hour_12) {
		this.hour_12 = hour_12;
	}

	public double getHour_24() {
		return hour_24;
	}

	public void setHour_24(double hour_24) {
		this.hour_24 = hour_24;
	}

	public Timestamp getLasttime() {
		return lasttime;
	}

	public void setLasttime(Timestamp lasttime) {
		this.lasttime = lasttime;
	}

	public double getMin_10() {
		return min_10;
	}

	public void setMin_10(double min_10) {
		this.min_10 = min_10;
	}
}
