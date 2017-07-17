package com.wavegis.engin.db.flood_check;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class FloodAlertEnginView extends SimpleEnginView {
	
	private static final String enginID = FloodAlertEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
