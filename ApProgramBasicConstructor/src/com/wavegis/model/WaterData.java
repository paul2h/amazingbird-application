package com.wavegis.model;

import java.sql.Timestamp;

public class WaterData {

	private String stid;
	private String stname;
	private Timestamp lasttime;
	private double voltage;
	private double waterlevel;
	private double rainfall10min;

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

	public double getRainfall10min() {
		return rainfall10min;
	}

	public void setRainfall10min(double rainfall10min) {
		this.rainfall10min = rainfall10min;
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
