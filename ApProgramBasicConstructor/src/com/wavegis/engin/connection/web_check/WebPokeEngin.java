package com.wavegis.engin.connection.web_check;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.ConnectWebTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.FocusData;

public class WebPokeEngin extends TimerEngin {

	public static final String enginID = "WebPokeEngin";
	private static final String enginName = "網頁正常監控Engin";
	private static final WebPokeEnginView enginView = new WebPokeEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WebPokeEngin() {
		setTimeout(1000 * 30);// TODO
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
		String urlString = "";
		for (FocusData focusData : new ArrayList<FocusData>()) {// TODO
			if (focusData.isHas_url()) {
				showMessage("\n專案 : " + focusData.getProject_name());
				urlString = focusData.getWeb_url();
				String result = ConnectWebTool.tryConnect(urlString);
				showMessage("--- 測試結果 --- \n " + result);
				if (result.indexOf("OK!") < 0) {// 連到網頁有問題
					showMessage("放入警告待發送Queue.");
					// TODO 
				}
			}
		}
		showMessage("--- 本次測試結束.  " + sdf.format(new Date()) + " ---");

	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
