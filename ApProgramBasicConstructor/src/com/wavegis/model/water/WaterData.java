package com.wavegis.model.water;

import java.sql.Timestamp;

public class WaterData {

	private String stid;
	private String stname;
	private Timestamp lasttime;
	private double voltage;
	private double waterlevel;
	private double 	temperature;
	private double rainfall10min;
	private double rainfall1hour;
	private double rainfall3hour;
	private double rainfall6hour;
	private double rainfall12hour;
	private double rainfall24hour;
	private double rainfallCounter;
	private double bottom_height;

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

	public double getBottom_height() {
		return bottom_height;
	}

	public void setBottom_height(double bottom_height) {
		this.bottom_height = bottom_height;
	}

	public double getRainfall1hour() {
		return rainfall1hour;
	}

	public void setRainfall1hour(double rainfall1hour) {
		this.rainfall1hour = rainfall1hour;
	}

	public double getRainfall3hour() {
		return rainfall3hour;
	}

	public void setRainfall3hour(double rainfall3hour) {
		this.rainfall3hour = rainfall3hour;
	}

	public double getRainfall6hour() {
		return rainfall6hour;
	}

	public void setRainfall6hour(double rainfall6hour) {
		this.rainfall6hour = rainfall6hour;
	}

	public double getRainfall12hour() {
		return rainfall12hour;
	}

	public void setRainfall12hour(double rainfall12hour) {
		this.rainfall12hour = rainfall12hour;
	}

	public double getRainfall24hour() {
		return rainfall24hour;
	}

	public void setRainfall24hour(double rainfall24hour) {
		this.rainfall24hour = rainfall24hour;
	}

	public double getRainfallCounter() {
		return rainfallCounter;
	}

	public void setRainfallCounter(double rainfallCounter) {
		this.rainfallCounter = rainfallCounter;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
}
