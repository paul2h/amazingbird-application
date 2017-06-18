package com.wavegis.engin.db.fake;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class FakeGpsCarUpdateEngin extends TimerEngin {
	public static final String enginID = "FakeCarUpdate";
	private static final String enginName = "車機假資料更新1.0";
	private static final EnginView enginView = new FakeGpsCarUpdateEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public FakeGpsCarUpdateEngin() {
		super();
		setTimeout(Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimePeriod_Fake_GPS")));
	}

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

	@Override
	public boolean startEngin() {
		int targetRunHour = Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("Fake_GPS_Target_Run_hour"));
		showMessage(String.format("每日 %d到 %d點進行假資料更新.", (targetRunHour - 1), targetRunHour));
		return super.startEngin();
	}

	@Override
	public void timerAction() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) == Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("Fake_GPS_Target_Run_hour"))) {
			showMessage("進行假資料更新..." + simpleDateFormat.format(calendar.getTime()));
			FakeGpsDao.getInstance().updateGpsData();
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
