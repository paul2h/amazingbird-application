package com.kiwi.basic;

public interface Engin {

	public String getEnginID();
	
	public String getEnginName();
	
	public EnginView getEnginView();
	
	public boolean isStarted();
	
	public boolean startEngin();
	
	public boolean stopEngin();
	
}
