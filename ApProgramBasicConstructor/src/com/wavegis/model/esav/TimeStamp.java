package com.wavegis.model.esav;

public class TimeStamp {
	private String DateTime;
	private int OffsetMinutes;
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public int getOffsetMinutes() {
		return OffsetMinutes;
	}
	public void setOffsetMinutes(int offsetMinutes) {
		OffsetMinutes = offsetMinutes;
	}
	
}
