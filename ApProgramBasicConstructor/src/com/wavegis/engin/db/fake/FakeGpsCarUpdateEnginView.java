package com.wavegis.engin.db.fake;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class FakeGpsCarUpdateEnginView extends SimpleEnginView{
	
	private static final String enginID = FakeGpsCarUpdateEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
