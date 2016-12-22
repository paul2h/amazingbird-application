package com.wavegis.engin.cctv;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class CCTVEngin extends TimerEngin {

	private static final String enginID = "CCTV";
	private static final String enginName = "CCTV讀取Engin";
	private static final CCTVEnginView enginView = new CCTVEnginView();
	private Logger logger;

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

	@Override
	public void timerAction() {
		showMessage("開始取得影像...");
		for (CCTVData cctvData : new ArrayList<CCTVData>()) {// TODO 需放入實際CCTV清單資料
			showMessage(cctvData.getStname() + "...");
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
		showMessage("影像取得結束.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
