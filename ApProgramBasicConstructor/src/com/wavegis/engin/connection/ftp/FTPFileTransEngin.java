package com.wavegis.engin.connection.ftp;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.FTPTool;
import com.wavegis.global.tools.LogTool;

public class FTPFileTransEngin extends TimerEngin {

	public static final String enginID = "FTP_TRANS";
	private static final String enginName = "FTP檔案傳輸Engin";
	private static final FTPFileTransEnginView enginView = new FTPFileTransEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	public FTPFileTransEngin() {
		setTimeout(1000 * 60);
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
		String FtpSendDir = GlobalConfig.CONFPIG_PROPERTIES.getProperty("FtpSendDir");
		String[] FtpSendFiles = GlobalConfig.CONFPIG_PROPERTIES.getProperty("FtpSendFiles").split(",");
		for (int i = 0; i < FtpSendFiles.length; i++) {
			String fileName = FtpSendFiles[i];
			String filePath = FtpSendDir + "/" + fileName;

			showMessage("發送檔案..." + filePath);
			FTPTool.UploadFile("13.76.255.253", "/", "wra02", "wavegis", filePath, fileName, true);
			showMessage("發送完成 :" + filePath);
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
