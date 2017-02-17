package com.wavegis.engin.notification.gmail;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class MailSendEnginView extends SimpleEnginView{
	
	private static final String enginID = MailSendEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
