package com.wavegis.engin.connection.ws.cwb;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CWBDataFileGetEnginView extends SimpleEnginView{
	
	private static final String enginID = CWBDataFileGetEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
