package com.wavegis.engin.connection.web_check;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class WebPokeEnginView extends SimpleEnginView{
	
	private static final String enginID = WebPokeEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
