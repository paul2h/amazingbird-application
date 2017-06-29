package com.wavegis.engin.db.read_conf;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class DBConfigEnginView extends SimpleEnginView{

	private static final String enginID = DBConfigEngin.enginID;
	@Override
	public String getEnginID(){
		return enginID;
	}
}
