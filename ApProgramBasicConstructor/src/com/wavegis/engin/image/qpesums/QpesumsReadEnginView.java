package com.wavegis.engin.image.qpesums;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class QpesumsReadEnginView extends SimpleEnginView{
	
	private static final String enginID = QpesumsReadEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
