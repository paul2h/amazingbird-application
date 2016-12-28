package com.wavegis.engin.db;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class DBEnginView extends SimpleEnginView{

	private static final String enginID = "DB";
	@Override
	public String getEnginID(){
		return enginID;
	}
}
