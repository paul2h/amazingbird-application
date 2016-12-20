package com.kiwi.global;

import java.util.HashMap;

import com.kiwi.service.demo.DemoEngin;
import com.kiwi.basic.Engin;
import com.kiwi.basic.EnginView;


public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = { new DemoEngin() };

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
