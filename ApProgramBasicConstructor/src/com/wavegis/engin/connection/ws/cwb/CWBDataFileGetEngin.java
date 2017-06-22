package com.wavegis.engin.connection.ws.cwb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.HttpFileTool;
import com.wavegis.global.tools.LogTool;

public class CWBDataFileGetEngin extends TimerEngin {

	public static final String enginID = "CWBDataFileGet";
	private static final String enginName = "氣象局檔案抓取Engin";
	private static final CWBDataFileGetEnginView enginView = new CWBDataFileGetEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	String targetDir = "D://"; // TODO

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
		try {
			getFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String tempURL = "http://opendata.cwb.gov.tw/opendataapi?dataid=W-C0034-001&authorizationkey=CWB-26655DD8-EAB9-4C56-8124-8F8827058FEC"; // TODO
	List<String> urlList = new ArrayList<>();// TODO

	private void getFiles() throws IOException {
		urlList.add(tempURL);// TODO
		// #[[ 取得檔案
		for (String url : urlList) {
			showMessage("開始取得檔案..." + url);
			String fileName = HttpFileTool.httpGetFile(url, targetDir);
			if (fileName.equals(HttpFileTool.NO_FILE_MESSAGE)) {
				showMessage("無檔案取得.");
			} else {
				showMessage("取得完成,位置:" + targetDir + fileName);
			}
		}
		// ]]
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
