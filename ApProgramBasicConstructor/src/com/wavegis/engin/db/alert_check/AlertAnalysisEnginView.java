package com.wavegis.engin.db.alert_check;

import com.wavegis.engin.prototype.SimpleEnginView;


@SuppressWarnings("serial")
public class AlertAnalysisEnginView extends SimpleEnginView {

	private static final String enginID = AlertAnalysisEngin.enginID;
	
	@Override
	public String getEnginID() {
		return enginID;
	}

}
