package com.wavegis.engin.connection.ws.others.maoli;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class MaoliWebSeriveEnginView extends SimpleEnginView{

	private static final String enginID = MaoliWebSeriveEngin.enginID;
	
	@Override
	public String getEnginID() {
		return enginID;
	}

}
