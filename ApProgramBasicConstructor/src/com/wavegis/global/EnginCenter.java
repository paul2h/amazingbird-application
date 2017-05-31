package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.connection.web_check.WebPokeEngin;
import com.wavegis.engin.notification.gmail.MailSendEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;


public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = { 
			new WebPokeEngin(),
			new MailSendEngin()
	};

	@SuppressWarnings("serial")
	public static final HashMap<String, EnginView> EnginViewMap = new HashMap<String, EnginView>() {
		{
			for (Engin engin : Engins) {
				put(engin.getEnginID(), engin.getEnginView());
			}
		}
	};

	public static final EnginView[] EnginViews = (EnginView[]) EnginViewMap.values()
			.toArray(new EnginView[Engins.length]);

}
