package com.wavegis.engin.connection.ws.soap.wavegis;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class WavegisWSEnginView extends SimpleEnginView{

	private static final String enginID = "WavegisWS";
	
	@Override
	public String getEnginID() {
		return enginID;
	}

}
