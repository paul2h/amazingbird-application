package com.wavegis.model.esav;

public class Data {
	private String State;
	private TimeStamp TimeStamp;
	private String TimeStampIso;
	private Double Value;
	
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public TimeStamp getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(TimeStamp timeStamp) {
		TimeStamp = timeStamp;
	}
	public String getTimeStampIso() {
		return TimeStampIso;
	}
	public void setTimeStampIso(String timeStampIso) {
		TimeStampIso = timeStampIso;
	}
	public Double getValue() {
		return Value;
	}
	public void setValue(Double value) {
		Value = value;
	}
	
}	
