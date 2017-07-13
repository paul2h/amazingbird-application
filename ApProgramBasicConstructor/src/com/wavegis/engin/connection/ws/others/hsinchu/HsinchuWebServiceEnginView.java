package com.wavegis.engin.connection.ws.others.hsinchu;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class HsinchuWebServiceEnginView extends SimpleEnginView{

	private static final String enginID = HsinchuWebServiceEngin.enginID;
	
	@Override
	public String getEnginID() {
		return enginID;
	}

}
