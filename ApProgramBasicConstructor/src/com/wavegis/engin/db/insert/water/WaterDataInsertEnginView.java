package com.wavegis.engin.db.insert.water;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class WaterDataInsertEnginView extends SimpleEnginView {

	private static final String enginID = WaterDataInsertEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
