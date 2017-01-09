package com.wavegis.engin.qpesums;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.QPESUMSTool;

public class QpesumsReadEngin extends TimerEngin {

	public static final String enginID = "QPESUMS";
	private static final String enginName = "QpesumsEngin";
	private static final QpesumsReadEnginView enginView = new QpesumsReadEnginView();
	private Logger logger = LogTool.getLogger(QpesumsReadEngin.class.getName());

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
		String gzFilePath = getLastFile("D:\\QP\\original");
		showMessage("開始分析產圖 : " + gzFilePath);
		QPESUMSTool.transDataProcess(gzFilePath);
		showMessage("資料讀取完成.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	/** 掃過所指定的資料夾 看有哪些檔案跟資料夾 */
	private String getLastFile(String dirPath) {
		String tmpXlsDir = dirPath;// excel檔暫存的資料夾位子
		File dir = new File(tmpXlsDir);
		String absolutePath = dir.getAbsolutePath();
		String[] fileList = dir.list();
		return absolutePath + "\\" + fileList[fileList.length - 1];
	}

}
