package com.wavegis.engin.image.http_image_typhoon;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.ConnectWebTool;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;

public class TyphoonImageEngin extends TimerEngin {

	public static final String enginID = "TyphoonImage";
	private static final String enginName = "颱風圖片接收(未完成)";
	private static final EnginView enginView = new TyphoonImageEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	private SimpleDateFormat satelliteTimeFormat = new SimpleDateFormat(
			GlobalConfig.XML_CONFIG.getProperty("satelliteTimeFormat"));

	private SimpleDateFormat ecmwfTyphoonTimeFormat = new SimpleDateFormat(
			GlobalConfig.XML_CONFIG.getProperty("ecmwfTyphoonTimeFormat"));

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
		showMessage("圖片接收開始...");
		Calendar calendar = Calendar.getInstance();
		// #[[ 氣象雲圖抓取
		showMessage("接收氣象雲圖...");
		/* 取得現在時間 */
		calendar = Calendar.getInstance();
		/* 開始抓取前半小時圖片 */
		for (int i = 0; i < 3; i++) {
			// 往前十分鐘
			calendar.add(Calendar.MINUTE, -10);
			// 取整數
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) / 10 * 10);
			// 產生URL並測試
			String urlString = GlobalConfig.XML_CONFIG.getProperty("SATELLITE_URL_PREFIX")
					+ satelliteTimeFormat.format(calendar.getTime())
					+ GlobalConfig.XML_CONFIG.getProperty("SATELLITE_URL_SUFFIX");
			String saveImagePath = GlobalConfig.XML_CONFIG.getProperty("SATELLITE_IMAGE_PATH")
					+ satelliteTimeFormat.format(calendar.getTime())
					+ GlobalConfig.XML_CONFIG.getProperty("SATELLITE_URL_SUFFIX");
			showMessage("URL : " + urlString);
			showMessage("儲存位置  : " + saveImagePath);
			try {
				HttpImageTool.getImage(urlString, saveImagePath);
			} catch (Exception e) {

			}
		}
		// ]]
		// #[[ ECMWF 颱風資訊圖抓取
		showMessage("接收ECMWF颱風資訊圖...");
		/* 取得現在時間 */
		calendar = Calendar.getInstance();
		/* 開始抓往後十天的圖片 */
		String todayString = ecmwfTyphoonTimeFormat.format(calendar.getTime()) + "00";
		String targetString;
		int hourParam = 0;
		for (int i = 0; i < 10; i++) {
			hourParam = i * 24;
			if (i > 0) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			targetString = ecmwfTyphoonTimeFormat.format(calendar.getTime()) + "00";
			String urlString = GlobalConfig.XML_CONFIG.getProperty("TYPHOON_ECMWF_URL_PREFIX") + todayString + ","
					+ hourParam + "," + targetString;
			// 取得原始網頁文字 從中抓出圖片URL
			String originalWebString = ConnectWebTool.getWebPageText(urlString);
			int urlPosition_start = originalWebString.indexOf("http://stream.ecmwf.int/data/");
			int urlPosition_end = originalWebString.substring(urlPosition_start).indexOf(".png") + 4
					+ urlPosition_start;
			String realImageUrlString = originalWebString.substring(urlPosition_start, urlPosition_end);
			String imageSavePath = GlobalConfig.XML_CONFIG.getProperty("TYPHOON_ECMWF_IMAGE_PATH") 
					+ i + ".gif";
			showMessage("URL : "+realImageUrlString);
			showMessage("儲存位置 :"+imageSavePath);
			try {
				HttpImageTool.getImage(realImageUrlString, imageSavePath);
			} catch (IOException e) {
			}
		}
		// ]]

		// #[[ JTWC 颱風資訊圖抓取
//		showMessage("接收JTWC颱風資訊圖...");
//		String urlString = GlobalConf.TYPHOON_JTWC_URL;
//		showMessage("測試連結 : " + urlString);
//		showMessage(">>"
//				+ service.tryGetImageProcess_customName(urlString, GlobalConf.TYPHOON_JTWC_IMAGE_PATH, "jtwc.gif"));
		// ]]
	}

	@Override
	public void showMessage(String message) {
		logger.info(message);
		enginView.showMessage(message);
	}

}
