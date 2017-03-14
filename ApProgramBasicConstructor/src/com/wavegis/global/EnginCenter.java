package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.connection.ws.others.hsinchu.HsinchuWebServiceEngin;
import com.wavegis.engin.connection.ws.others.maoli.MaoliWebSeriveEngin;
import com.wavegis.engin.connection.ws.others.taoyuan.TaoyuanWebServiceEngin;
import com.wavegis.engin.connection.ws.soap.center.CenterWSEngin;
import com.wavegis.engin.db.insert.raw.RawDataInsertEngin;
import com.wavegis.engin.db.query.preload.PreloadEngin;
import com.wavegis.engin.image.cctv.hsinchu.HsinchuCCTVEngin;
import com.wavegis.engin.image.cctv.maoli.MaoliCCTVEngin;
import com.wavegis.engin.image.trans.ImageTransClientEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new PreloadEngin()
			, new CenterWSEngin()
			, new RawDataInsertEngin()
			, new TaoyuanWebServiceEngin()
			, new MaoliWebSeriveEngin()
			, new HsinchuWebServiceEngin()
			//, new HsinchuCityWebServiceEngin()
			, new MaoliCCTVEngin()
			, new HsinchuCCTVEngin()
			, new ImageTransClientEngin()
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
