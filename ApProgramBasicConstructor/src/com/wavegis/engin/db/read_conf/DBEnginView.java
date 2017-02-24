package com.wavegis.engin.db.read_conf;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class DBEnginView extends SimpleEnginView{

	private static final String enginID = "DB";
	@Override
	public String getEnginID(){
		return enginID;
	}
}
