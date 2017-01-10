package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.engin.cctv.hsinchu.HsinchuCCTVEngin;
import com.wavegis.engin.cctv.maoli.MaoliCCTVEngin;
import com.wavegis.engin.insert.raw.RawDataInsertEngin;
import com.wavegis.engin.ws.center.CenterWSEngin;
import com.wavegis.engin.ws.hsinchu.HsinchuWebServiceEngin;
import com.wavegis.engin.ws.maoli.MaoliWebSeriveEngin;
import com.wavegis.engin.ws.taoyuan.TaoyuanWebServiceEngin;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new CenterWSEngin()
			, new RawDataInsertEngin()
			, new TaoyuanWebServiceEngin()
			, new MaoliWebSeriveEngin()
			, new HsinchuWebServiceEngin()
			//, new HsinchuCityWebServiceEngin()
			, new MaoliCCTVEngin()
			, new HsinchuCCTVEngin()
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
