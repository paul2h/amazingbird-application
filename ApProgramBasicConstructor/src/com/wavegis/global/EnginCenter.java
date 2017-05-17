package com.wavegis.global;

import java.util.HashMap;

import com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans.RawDataReceiveEngin;
import com.wavegis.engin.db.insert.water.WaterDataInsertEngin;
import com.wavegis.engin.db.read_conf.DBEngin;
import com.wavegis.engin.image.cctv.from_fold.ImageEngin;
import com.wavegis.engin.image.cctv.mjpeg.CCTVEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.raw_data_analysis.kenkul.KenkulDataEngin;

public class EnginCenter {
	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = {
			new CCTVEngin(),
			new ImageEngin(),
			new DBEngin(),
			new RawDataReceiveEngin(),
			new KenkulDataEngin(),
			new WaterDataInsertEngin()
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
