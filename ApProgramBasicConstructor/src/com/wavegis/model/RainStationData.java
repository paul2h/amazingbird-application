package com.wavegis.model;

import java.sql.Timestamp;

/** 雨量站 */
public class RainStationData {
	private String stid;
	private Timestamp datatime;
	private Double rain;
	private Double hour_3;
	private Double hour_6;
	private Double hour_12;
	private Double hour_24;

	/* 安研eSAVE所含參數 */
	private String PQID;
	private String name;
	
	/* 安研eSAVE所含參數 - END*/
	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public Timestamp getDatatime() {
		return datatime;
	}

	public void setDatatime(Timestamp datatime) {
		this.datatime = datatime;
	}

	public Double getRain() {
		return rain;
	}

	public void setRain(Double rain) {
		this.rain = rain;
	}

	public Double getHour_3() {
		return hour_3;
	}

	public void setHour_3(Double hour_3) {
		this.hour_3 = hour_3;
	}

	public Double getHour_6() {
		return hour_6;
	}

	public void setHour_6(Double hour_6) {
		this.hour_6 = hour_6;
	}

	public Double getHour_12() {
		return hour_12;
	}

	public void setHour_12(Double hour_12) {
		this.hour_12 = hour_12;
	}

	public Double getHour_24() {
		return hour_24;
	}

	public void setHour_24(Double hour_24) {
		this.hour_24 = hour_24;
	}

	public String getPQID() {
		return PQID;
	}

	public void setPQID(String pQID) {
		PQID = pQID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
