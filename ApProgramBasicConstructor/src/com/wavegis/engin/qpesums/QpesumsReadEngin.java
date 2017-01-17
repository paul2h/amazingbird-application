package com.wavegis.engin.qpesums;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.QPESUMSTool;

public class QpesumsReadEngin extends TimerEngin {

	public static final String enginID = "QPESUMS";
	private static final String enginName = "QpesumsEngin";
	private static final QpesumsReadEnginView enginView = new QpesumsReadEnginView();
	private Logger logger = LogTool.getLogger(QpesumsReadEngin.class.getName());

	private static String gzDirPath = GlobalConfig.XML_CONFIG.getProperty("gzDirPath");
	
	private static String QPESUMS_KEYWORD = "qpfqpe_060min";

	private static String map_image_path = GlobalConfig.XML_CONFIG.getProperty("map_image_path");
	private static String boundaryMapImagePath = GlobalConfig.XML_CONFIG.getProperty("boundaryMapImagePath");

	private static String qp_pngFilePath = GlobalConfig.XML_CONFIG.getProperty("qp_pngFilePath");
	private static String qp_outputResultPath = GlobalConfig.XML_CONFIG.getProperty("qp_outputResultPath");
	private static String oneHR_pngFilePath = GlobalConfig.XML_CONFIG.getProperty("oneHR_pngFilePath");
	private static String oneHR_outputResultPath = GlobalConfig.XML_CONFIG.getProperty("oneHR_outputResultPath");
	
	private static String ONE_HOUR_RAD_KEYWORD = "CB_GC_PCP_1H_RAD";
	

	public QpesumsReadEngin() {
		setTimeout(1000 * 30);
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
		showMessage("開始取得QPESUMS資料...");
		String gzFilePath = getLastFile(gzDirPath , QPESUMS_KEYWORD);
		showMessage("開始分析產圖 : " + gzFilePath);
		QPESUMSTool.transDataProcess(gzFilePath, qp_pngFilePath, map_image_path, boundaryMapImagePath, qp_outputResultPath);
		showMessage("資料讀取完成.");
		
		showMessage("開始取得過去一小時雷達資料...");
		gzFilePath = getLastFile(gzDirPath , ONE_HOUR_RAD_KEYWORD);
		showMessage("開始分析產圖 : " + gzFilePath);
		QPESUMSTool.transDataProcess(gzFilePath, oneHR_pngFilePath, map_image_path, boundaryMapImagePath, oneHR_outputResultPath);
		showMessage("資料讀取完成.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	/** 掃過所指定的資料夾 抓出最後一個檔案(指定檔名關鍵字) */
	private String getLastFile(String dirPath, String fileNameKeyWord) {
		String tmpXlsDir = dirPath;
		final String fileName = fileNameKeyWord;
		File dir = new File(tmpXlsDir);
		String absolutePath = dir.getAbsolutePath();
		String[] fileList = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.indexOf(fileName) >= 0)
					return true;
				else
					return false;
			}
		});
		return absolutePath + "\\" + fileList[fileList.length - 1];
	}
}
