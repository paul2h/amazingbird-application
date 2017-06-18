package com.wavegis.engin.db.data_check;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class PokerDBEnginView extends SimpleEnginView {
	
	private static final String enginID = PokerDBEngin.enginID;
	
	@Override
	public String getEnginID() {
		return enginID;
	}
	

}
