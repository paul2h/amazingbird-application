package com.wavegis.model.gps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wavegis.global.GlobalConfig;

public class GpsData implements Comparable<GpsData> {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
	private String originalMessage = "";// 原始訊息
	private String _uid = "";
	private String unit_name = "";
	private String status = "";
	private Date datatime = null;
	private double lon = 0.0;
	private double lat = 0.0;
	private double lat_deg = 0.0;
	private double lat_min = 0.0;
	private double lon_deg = 0.0;
	private double lon_min = 0.0;
	private double speed = 0.0;
	private double course = 0.0;
	private int sat_count = 0;// 衛星數
	private String event = "";
	private int type = 0;
	private String codeValue = "";
	private String input = "";
	private double roundSpeed = 0.0;// 轉速
	private double voltage = 0.0;// 電壓
	private double battery_voltage = 0.0;//備援電池電壓

	/* 由DB回傳的欄位 */
	private String car_id = "";
	private String locate_county = "";
	private String locate_town = "";
	private String locate_road = "";

	/* 由接收資料時設定的欄位 */
	private String ip = "";
	private int port = 0;
	private Date receiveTime = null;
	
	public GpsData(){}
	
	public GpsData(String customString) throws ParseException {
		String[] splitString = customString.split(",");
		this._uid = splitString[2];
		this.datatime = sdf.parse(splitString[3]);
		this.status = splitString[4];
		this.lon = Double.valueOf(splitString[5]);
		this.lat = Double.valueOf(splitString[6]);
		this.speed = Double.valueOf(splitString[7]);
		this.course = Double.valueOf(splitString[8]);
		this.sat_count = Integer.valueOf(splitString[9]);
		this.event = splitString[10];
		this.input = splitString[11];
		this.roundSpeed = Double.valueOf(splitString[12]);
		this.voltage = Double.valueOf(splitString[13]);
	}

	public String getOriginalMessage() {
		return originalMessage;
	}

	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}

	public String get_uid() {
		return _uid;
	}

	public void set_uid(String _uid) {
		this._uid = _uid;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		if (lat_deg != 0.0 && lat_min != 0.0)
			lat = lat_deg + (lat_min / 60);
		return lat;
	}

	public double getLat_deg() {
		return lat_deg;
	}

	public void setLat_deg(double lat_deg) {
		this.lat_deg = lat_deg;
	}

	public double getLat_min() {
		return lat_min;
	}

	public void setLat_min(double lat_min) {
		this.lat_min = lat_min;
	}

	public double getLon() {
		if (lon_deg != 0.0 && lon_min != 0.0)
			lon = lon_deg + (lon_min / 60);
		return lon;
	}

	public double getLon_deg() {
		return lon_deg;
	}

	public void setLon_deg(double lon_deg) {
		this.lon_deg = lon_deg;
	}

	public double getLon_min() {
		return lon_min;
	}

	public void setLon_min(double lon_min) {
		this.lon_min = lon_min;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getCourse() {
		return course;
	}

	public void setCourse(double course) {
		this.course = course;
	}

	public int getSat_count() {
		return sat_count;
	}

	public void setSat_count(int sat_count) {
		this.sat_count = sat_count;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public double getRoundSpeed() {
		return roundSpeed;
	}

	public void setRoundSpeed(double roundSpeed) {
		this.roundSpeed = roundSpeed;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}

	public String getLocate_town() {
		return locate_town;
	}

	public void setLocate_town(String locate_town) {
		this.locate_town = locate_town;
	}

	public String getLocate_county() {
		return locate_county;
	}

	public void setLocate_county(String locate_county) {
		this.locate_county = locate_county;
	}

	public String getLocate_road() {
		return locate_road;
	}

	public void setLocate_road(String locate_road) {
		this.locate_road = locate_road;
	}

	public String getCar_id() {
		return car_id;
	}

	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Override
	public String toString() {
		String result = String.format("$,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,#",
				GlobalConfig.CustomMessageKey,
				_uid,
				sdf.format(datatime),
				status,
				lon,
				lat,
				speed,
				course,
				sat_count,
				event,
				input,
				roundSpeed,
				voltage);
		return result;
	}

	@Override
	public int compareTo(GpsData o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getBattery_voltage() {
		return battery_voltage;
	}

	public void setBattery_voltage(double battery_voltage) {
		this.battery_voltage = battery_voltage;
	}
}
