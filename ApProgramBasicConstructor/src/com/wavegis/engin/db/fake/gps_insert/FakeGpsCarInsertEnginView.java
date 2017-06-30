package com.wavegis.engin.db.fake.gps_insert;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class FakeGpsCarInsertEnginView extends SimpleEnginView{
	
	private static final String enginID = FakeGpsCarInsertEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
