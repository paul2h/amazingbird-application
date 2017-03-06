package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.connection.tcp.socket.jian_hua.SensorReceivingEngin;
import com.wavegis.engin.db.insert.water.WaterDataInsertEngin;
import com.wavegis.engin.db.select.DBConfigEngin;
import com.wavegis.engin.image.cctv.from_fold.ImageEngin;
import com.wavegis.engin.image.cctv.mjpeg.CCTVEngin;
import com.wavegis.engin.image.qpesums.QpesumsReadEngin;
import com.wavegis.engin.notification.alert_check.AlertAnalysisEngin;
import com.wavegis.engin.notification.sms.SMSSendEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new DBConfigEngin()
			, new CCTVEngin()
			, new ImageEngin()
			, new AlertAnalysisEngin()
			, new SMSSendEngin()
			, new QpesumsReadEngin()
			, new SensorReceivingEngin()
			, new WaterDataInsertEngin()
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
