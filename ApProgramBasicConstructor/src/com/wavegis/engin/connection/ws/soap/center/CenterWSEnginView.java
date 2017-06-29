package com.wavegis.engin.connection.ws.soap.center;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CenterWSEnginView extends SimpleEnginView{
	
	private static final String enginID = CenterWSEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
