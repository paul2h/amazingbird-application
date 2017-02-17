package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.connection.web_check.WebPokeEngin;
import com.wavegis.engin.connection.ws.others.ConvertToBeanEngin;
import com.wavegis.engin.connection.ws.soap.center.CenterWSEngin;
import com.wavegis.engin.connection.ws.soap.wavegis.WavegisWSEngin;
import com.wavegis.engin.db.alert_check.AlertAnalysisEngin;
import com.wavegis.engin.db.insert.rain.RainDataInsertEngin;
import com.wavegis.engin.db.insert.raw.RawDataInsertEngin;
import com.wavegis.engin.db.read_conf.DBEngin;
import com.wavegis.engin.image.CCTVEngin;
import com.wavegis.engin.image.FakeImageEngin;
import com.wavegis.engin.image.ImageEngin;
import com.wavegis.engin.image.ImageTransClientEngin;
import com.wavegis.engin.image.ImageTransServerEngin;
import com.wavegis.engin.image.qpesums.QpesumsReadEngin;
import com.wavegis.engin.notification.sms.SMSSendEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new CCTVEngin(),
			new WavegisWSEngin(),
			new RainDataInsertEngin(),
			new RawDataInsertEngin(),
			new ConvertToBeanEngin(),
			new CenterWSEngin(),
			new AlertAnalysisEngin(),
			new SMSSendEngin(),
			new QpesumsReadEngin(),
			new ImageEngin(),
			new FakeImageEngin(),
			new DBEngin(),
			new ImageTransServerEngin(),
			new ImageTransClientEngin(),
			new WebPokeEngin()
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
