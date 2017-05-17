package com.wavegis.engin.raw_data_analysis.kenkul;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class KenkulDataEnginView extends SimpleEnginView{
	
	public static final String enginID = KenkulDataEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
