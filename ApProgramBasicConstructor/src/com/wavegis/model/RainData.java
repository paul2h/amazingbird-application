package com.wavegis.model;

import java.sql.Timestamp;

public class RainData {
	private String stid;// 雨量站編號
	private String stname;// 站名
	private Timestamp lasttime;
	private String time;
	private double rain_current;
	private double min_10;
	private double hour_1;
	private double hour_3;
	private double hour_6;
	private double hour_12;
	private double hour_24;
	private double hour_36;
	private double hour_72;
	private double voltage;

	/** 物件名稱 */
	public String getClass_name() {
		return "rain_station";
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public String getStname() {
		return stname;
	}

	public void setStname(String stname) {
		this.stname = stname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public double getRain_current(){
		return rain_current;
	}

	public void setRain_current(double rain_current){
		this.rain_current = rain_current;
	}

	public double getHour_1(){
		return hour_1;
	}

	public void setHour_1(double hour_1){
		this.hour_1 = hour_1;
	}

	public double getHour_36(){
		return hour_36;
	}

	public void setHour_36(double hour_36){
		this.hour_36 = hour_36;
	}

	public double getHour_72(){
		return hour_72;
	}

	public void setHour_72(double hour_72){
		this.hour_72 = hour_72;
	}

	public double getVoltage(){
		return voltage;
	}

	public void setVoltage(double voltage){
		this.voltage = voltage;
	}
}
