package com.wavegis.engin.warn.mail;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class MailSendEnginView extends SimpleEnginView{
	
	private static final String enginID = MailSendEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
