package com.wavegis.engin.db.alert_check;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.SMSAlertData;

public class AlertAnalysisEngin implements Engin {

	public static final String enginID = "alertAnalysis";
	private static final String enginName = "水位警戒分析Engin";
	private boolean isStarted = false;
	private Logger logger;
	private TimerTask timerTask;

	private static final AlertAnalysisEnginView enginView = new AlertAnalysisEnginView();
	private AlertDao dao;

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public boolean startEngin() {
		showMessage("初始化DAO");
		try {
			dao = new AlertDao();
		} catch (IOException e) {
			showMessage("DAO初始化失敗");
			e.printStackTrace();
			return false;
		}

		showMessage("Engin開啟");
		timerTask = new TimerTask() {
			@Override
			public void run() {
				List<SMSAlertData> alertDatas = dao.getAlertData();
				for (SMSAlertData alertData : alertDatas) {
					showMessage(alertData.getStid() + " " + alertData.getStnm() + " " + alertData.getPhones());

					// 檢查該站是否已在待送清單中
					Calendar before12hourCalendar = Calendar.getInstance();
					before12hourCalendar.set(Calendar.HOUR_OF_DAY, before12hourCalendar.get(Calendar.HOUR_OF_DAY) - 12);
					SMSAlertData dataInList = ProxyData.SMS_SEND_LIST.get(alertData.getStid());
					if (dataInList == null
							|| dataInList.getAlert_value() < alertData.getAlert_value()
							|| ((dataInList.getDatatime().before(before12hourCalendar.getTime())) && alertData.getDatatime().after(before12hourCalendar.getTime()))){
						showMessage("放入待送清單中 : " + alertData.getStnm() + " - 警戒值 : " + alertData.getAlert_value());
						ProxyData.SMS_SEND_LIST.put(alertData.getStid(), alertData);
					} else {
						showMessage("已在待送清單 : " + alertData.getStnm());
					}

				}
			}
		};

		new Timer().schedule(timerTask, 1000, 5000);
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("Engin關閉");
		boolean stopSuccess = false;
		if (timerTask != null) {
			stopSuccess = timerTask.cancel();
			timerTask = null;
		}
		isStarted = false;
		return stopSuccess;
	}

	private void showMessage(String message) {
		enginView.showMessage(message);
		if (logger == null) {
			logger = LogTool.getLogger(AlertAnalysisEngin.class.getName());
		}
		logger.info(message);
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

}
