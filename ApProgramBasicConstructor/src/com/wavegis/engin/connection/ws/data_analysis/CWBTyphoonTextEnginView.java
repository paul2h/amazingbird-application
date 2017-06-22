package com.wavegis.engin.connection.ws.data_analysis;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CWBTyphoonTextEnginView extends SimpleEnginView{
	
	private static final String enginID = CWBTyphoonTextEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
