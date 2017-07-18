package com.wavegis.engin.db.message;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class MessageEnginView extends SimpleEnginView {
	
	private static final String enginID = MessageEngin.enginID;

	@Override
	public String getEnginID() {
		// TODO Auto-generated method stub
		return enginID;
	}

}
