package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.engin.cctv.CCTVEngin;
import com.wavegis.engin.db.DBEngin;
import com.wavegis.engin.image.ImageEngin;
import com.wavegis.engin.warn.alert_check.AlertAnalysisEngin;
import com.wavegis.engin.warn.sms.SMSSendEngin;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new CCTVEngin(),
			new ImageEngin(),
			new DBEngin(),
			new AlertAnalysisEngin(),
			new SMSSendEngin()
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
