package com.wavegis.engin.connection.web_check;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class WebMonitorEnginView extends SimpleEnginView{
	
	private static final String enginID = WebMonitorEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
