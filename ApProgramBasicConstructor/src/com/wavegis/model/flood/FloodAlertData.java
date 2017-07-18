package com.wavegis.model.flood;

import java.util.Date;

public class FloodAlertData {

	private String flood_area_id;
	private Date datatime;
	private String datatime_long;
	private double area_range;
	private double waterlevel;
	private String stid;
	private String area_polygon_string;
	private boolean hasSend = false;

	public String getFlood_area_id() {
		return flood_area_id;
	}

	public void setFlood_area_id(String flood_area_id) {
		this.flood_area_id = flood_area_id;
	}

	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}

	public String getDatatime_long() {
		return datatime_long;
	}

	public void setDatatime_long(String datatime_long) {
		this.datatime_long = datatime_long;
	}

	public double getArea_range() {
		return area_range;
	}

	public void setArea_range(double area_range) {
		this.area_range = area_range;
	}

	public double getWaterlevel() {
		return waterlevel;
	}

	public void setWaterlevel(double waterlevel) {
		this.waterlevel = waterlevel;
	}

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public String getArea_polygon_string() {
		return area_polygon_string;
	}

	public void setArea_polygon_string(String area_polygon_string) {
		this.area_polygon_string = area_polygon_string;
	}

	public boolean isHasSend() {
		return hasSend;
	}

	public void setHasSend(boolean hasSend) {
		this.hasSend = hasSend;
	}

}
