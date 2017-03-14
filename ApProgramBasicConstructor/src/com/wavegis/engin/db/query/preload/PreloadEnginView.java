package com.wavegis.engin.db.query.preload;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class PreloadEnginView extends SimpleEnginView{

	private static final String enginID = "DBPreload";
	@Override
	public String getEnginID(){
		return enginID;
	}
}
