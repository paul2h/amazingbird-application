package com.wavegis.engin.image.cctv.mjpeg;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class CCTVEngin extends TimerEngin {

	public static final String enginID = "CCTV";
	private static final String enginName = "CCTV讀取1.1";
	private static final CCTVEnginView enginView = new CCTVEnginView();
	private Logger logger;
	private ConcurrentHashMap<String, Integer> threadCounts = new ConcurrentHashMap<>();
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public CCTVEngin() {
		int timeout = Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_CCTV"));
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

	@Override
	public void timerAction() {
		showMessage("開始取得影像..." + simpleDateFormat.format(Calendar.getInstance().getTime()));
		for (final CCTVData cctvData : GlobalConfig.CCTV_DATA_LIST) {
			int threadCount = 0;
			if (threadCounts.containsKey(cctvData.getStname())) {
				threadCount = threadCounts.get(cctvData.getStname());
			}
			showMessage(String.format("開啟接收Thread : %s (之前剩餘Thread數 : %d) \n %s", cctvData.getStname(), threadCount, cctvData.getURL()));
			threadCount++;
			threadCounts.put(cctvData.getStname(), threadCount);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (cctvData.isNeed_login()) {
							HttpImageTool.getAuthorizedImage(cctvData.getURL(), cctvData.getAccount(), cctvData.getPassword(), cctvData.getSavePath());
						} else {
							HttpImageTool.getImage(cctvData.getURL(), cctvData.getSavePath());
						}
						showMessage(cctvData.getStname() + " 影像取得成功.");
					} catch (IOException e) {
						showMessage(cctvData.getStname() + " 影像取得失敗.");
						e.printStackTrace();
					}
					threadCounts.put(cctvData.getStname(), threadCounts.get(cctvData.getStname()) - 1);
				}
			}).start();
		}

	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
