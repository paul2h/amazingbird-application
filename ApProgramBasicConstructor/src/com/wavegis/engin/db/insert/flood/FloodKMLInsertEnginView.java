package com.wavegis.engin.db.insert.flood;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class FloodKMLInsertEnginView extends SimpleEnginView{
	
	private static final String enginID = FloodKMLInsertEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
