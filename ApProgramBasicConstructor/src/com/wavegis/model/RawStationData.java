package com.wavegis.model;

import java.sql.Timestamp;

public class RawStationData {
	private String stno;
	private Timestamp datatime;
	private double waterlevel;
	
	/* 安研eSAVE所含參數 */
	private String PQID;
	private String name;
	
	/* 安研eSAVE所含參數 - END*/
	
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public Timestamp getDatatime() {
		return datatime;
	}
	public void setDatatime(Timestamp datatime) {
		this.datatime = datatime;
	}
	public double getWaterlevel() {
		return waterlevel;
	}
	public void setWaterlevel(double waterlevel) {
		this.waterlevel = waterlevel;
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
