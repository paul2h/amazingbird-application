package com.wavegis.model.flood;

import java.util.Date;

public class FloodLogData {

	private int log_id;
	private String flood_area_id;
	private String stid;
	private Date datatime;
	private double waterlevel;
	private double area_range;
	private String polygon_string;

	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public String getFlood_area_id() {
		return flood_area_id;
	}

	public void setFlood_area_id(String flood_area_id) {
		this.flood_area_id = flood_area_id;
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public double getWaterlevel() {
		return waterlevel;
	}

	public void setWaterlevel(double waterlevel) {
		this.waterlevel = waterlevel;
	}

	public double getArea_range() {
		return area_range;
	}

	public void setArea_range(double area_range) {
		this.area_range = area_range;
	}

	public String getPolygon_string() {
		return polygon_string;
	}

	public void setPolygon_string(String polygon_string) {
		this.polygon_string = polygon_string;
	}

	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}

}
