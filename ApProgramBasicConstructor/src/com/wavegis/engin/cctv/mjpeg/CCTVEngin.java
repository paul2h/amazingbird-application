package com.wavegis.engin.cctv.mjpeg;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class CCTVEngin extends TimerEngin {

	private static final String enginID = "CCTV";
	private static final String enginName = "CCTV讀取Engin";
	private static final CCTVEnginView enginView = new CCTVEnginView();
	private Logger logger;

	private Thread picGettingThread;

	public CCTVEngin() {
		int timeout =Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("CCTV_GET_TIME_PERIOD"));
		setTimeout(timeout);
		logger = LogTool.getLogger(CCTVEngin.class.getName());
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

	@SuppressWarnings("deprecation")
	@Override
	public void timerAction() {
		if (picGettingThread != null && picGettingThread.isAlive()) {
			showMessage("上次未跑完,殺掉Thread");
			picGettingThread.destroy();// TODO 待找方法修改
			picGettingThread = null;
		}

		showMessage("開始取得影像...");
		picGettingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				for (CCTVData cctvData : GlobalConfig.CCTV_DATA_LIST) {
					showMessage(cctvData.getStname() + "..." + cctvData.getURL());
					try {
						if (cctvData.isNeedLogin()) {
							HttpImageTool.getAuthorizedImage(cctvData.getURL(), cctvData.getAccount(), cctvData.getPassword(), cctvData.getSavePath());
						} else {
							HttpImageTool.getImage(cctvData.getURL(), cctvData.getSavePath());
						}
						showMessage(cctvData.getStname() + " 影像取得成功.");
					} catch (IOException e) {
						showMessage(cctvData.getStname() + " 影像取得失敗.");
						e.printStackTrace();
					}
				}
			}
		});
		picGettingThread.start();
		showMessage("影像取得結束.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
