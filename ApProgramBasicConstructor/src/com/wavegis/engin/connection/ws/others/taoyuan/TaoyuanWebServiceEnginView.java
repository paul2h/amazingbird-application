package com.wavegis.engin.connection.ws.others.taoyuan;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class TaoyuanWebServiceEnginView extends SimpleEnginView{

	private static final String enginID = TaoyuanWebServiceEngin.enginID;
	
	@Override
	public String getEnginID() {
		return enginID;
	}

}
