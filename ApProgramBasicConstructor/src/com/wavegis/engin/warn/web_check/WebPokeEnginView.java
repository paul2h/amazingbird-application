package com.wavegis.engin.warn.web_check;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class WebPokeEnginView extends SimpleEnginView{
	
	private static final String enginID = WebPokeEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
