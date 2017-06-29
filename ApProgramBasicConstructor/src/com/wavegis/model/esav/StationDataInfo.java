package com.wavegis.model.esav;

/** 測站所能取得的資料型別 */
public class StationDataInfo {
	private int DSChannelIndex;
	private int DSChannelType;
	private int DigitAccuracy;
	private String Name;
	private String PQCategory;
	private String PQId;
	private String Unit;
	
	public int getDSChannelIndex() {
		return DSChannelIndex;
	}
	public void setDSChannelIndex(int dSChannelIndex) {
		DSChannelIndex = dSChannelIndex;
	}
	public int getDSChannelType() {
		return DSChannelType;
	}
	public void setDSChannelType(int dSChannelType) {
		DSChannelType = dSChannelType;
	}
	public int getDigitAccuracy() {
		return DigitAccuracy;
	}
	public void setDigitAccuracy(int digitAccuracy) {
		DigitAccuracy = digitAccuracy;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPQCategory() {
		return PQCategory;
	}
	public void setPQCategory(String pQCategory) {
		PQCategory = pQCategory;
	}
	public String getPQId() {
		return PQId;
	}
	public void setPQId(String pQId) {
		PQId = pQId;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	
}
