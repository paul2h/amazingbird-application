package com.wavegis.model.esav;

public class StationInfo {
	private String Address;
	private Short Code;
	private Double LatitudeWGS84;
	private Double LongitudeWGS84;
	private String Name;
	private String STId;
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public Short getCode() {
		return Code;
	}
	public void setCode(Short code) {
		Code = code;
	}
	public Double getLatitudeWGS84() {
		return LatitudeWGS84;
	}
	public void setLatitudeWGS84(Double latitudeWGS84) {
		LatitudeWGS84 = latitudeWGS84;
	}
	public Double getLongitudeWGS84() {
		return LongitudeWGS84;
	}
	public void setLongitudeWGS84(Double longitudeWGS84) {
		LongitudeWGS84 = longitudeWGS84;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSTId() {
		return STId;
	}
	public void setSTId(String sTId) {
		STId = sTId;
	}
	
}
