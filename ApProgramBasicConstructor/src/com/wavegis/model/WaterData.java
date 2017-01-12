package com.wavegis.model;

import java.sql.Timestamp;

public class WaterData {
	private String stid;
	private String stname;
	private Timestamp lasttime;
	private double voltage;
	private double waterlevel;

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public Timestamp getLasttime() {
		return lasttime;
	}

	public void setLasttime(Timestamp lasttime) {
		this.lasttime = lasttime;
	}

	public double getWaterlevel() {
		return waterlevel;
	}

	public void setWaterlevel(double waterlevel) {
		this.waterlevel = waterlevel;
	}

	public String getStname() {
		return stname;
	}

	public void setStname(String stname) {
		this.stname = stname;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

}
