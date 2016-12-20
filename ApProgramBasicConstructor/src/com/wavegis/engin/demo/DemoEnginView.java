package com.wavegis.engin.demo;


import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class DemoEnginView extends SimpleEnginView {

	private static final String enginID = "demo";
	
	public DemoEnginView() {
		super();
	}

	@Override
	public String getEnginID() {
		return enginID;
	}

}
