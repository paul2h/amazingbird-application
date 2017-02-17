package com.wavegis.engin.image;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
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
		setTimeout(1000 * 5);
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
		if (picGettingThread != null || picGettingThread.isAlive()) {
			showMessage("上次未跑完,殺掉Thread");
			picGettingThread.destroy();// TODO 待找方法修改
			picGettingThread = null;
		}

		showMessage("開始取得影像...");
		picGettingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				for (CCTVData cctvData : new ArrayList<CCTVData>()) {// TODO 需放入實際CCTV清單資料
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
