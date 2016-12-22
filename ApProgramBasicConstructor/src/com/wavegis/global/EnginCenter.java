package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.engin.cctv.CCTVEngin;
import com.wavegis.engin.insert.rain.RainDataInsertEngin;
import com.wavegis.engin.insert.raw.RawDataInsertEngin;
import com.wavegis.engin.ws.wavegis.WavegisWSEngin;
import com.wavegis.engin.ws.wra.WraWSEngin;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new CCTVEngin(),
			new WavegisWSEngin(),
			new RainDataInsertEngin(),
			new RawDataInsertEngin(),
			new WraWSEngin()
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
