package com.wavegis.engin.qpesums;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class QpesumsReadEnginView extends SimpleEnginView{
	
	private static final String enginID = QpesumsReadEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
