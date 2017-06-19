package com.wavegis.engin.connection.web_check;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.ConnectWebTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.MailData;
import com.wavegis.model.WebMonitorFocusData;

public class WebMonitorEngin extends TimerEngin {

	public static final String enginID = "WebPokeEngin";
	private static final String enginName = "網頁正常監控1.0";
	private static final WebMonitorEnginView enginView = new WebMonitorEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WebMonitorEngin() {
		setTimeout(Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_WebMonitor", "60000")));
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
		showMessage("開始測試網頁程序..." + sdf.format(Calendar.getInstance().getTime()));
		for (WebMonitorFocusData focusData : GlobalConfig.WEB_MONITOR_URL_LIST) {
			if (focusData.isHas_url()) {
				checkConnectionProcess(focusData);
			} else {
				showMessage("\n專案 : " + focusData.getProject_name() + "  無網頁連結,跳過測試.");
			}
		}
		showMessage("例行網頁測試程序結束.");
	}

	private void checkConnectionProcess(WebMonitorFocusData focusData) {
		showMessage("\n專案 : " + focusData.getProject_name());
		String urlString = focusData.getWeb_url();
		String result = ConnectWebTool.tryConnect(urlString);
		showMessage("--- 測試結果 --- \n " + result);
		if (result.indexOf("OK!") < 0) {// 連到網頁有問題
			showMessage("放入警告待發送Queue.");
			addIntoMailSendQueue(focusData);
		}
		

	}
	
	private final String new_line_tag = "<br>";
	private void addIntoMailSendQueue(WebMonitorFocusData focusData){
		String subject = String.format("[%s] %s 網頁連線問題回報", focusData.getProject_name() , focusData.getProject_name());
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(new_line_tag + "網頁連線異常回報:");
		stringBuffer.append(new_line_tag + "異常專案:"+ focusData.getProject_name());
		stringBuffer.append(new_line_tag + "異常問題: 網頁無法連線 " + focusData.getWeb_url());
		stringBuffer.append(new_line_tag + "郵件發送時間:" + Calendar.getInstance().getTime());
		stringBuffer.append(new_line_tag + "回報人員:" + focusData.getReport_users());
		String mailContent = stringBuffer.toString();
		String targetMails = focusData.getReport_mails().replaceAll("\\{", "").replaceAll("\\}", "");
		MailData mailData = new MailData(subject, mailContent, null, targetMails);
		ProxyData.MAIL_SEND_QUEUE.offer(mailData);
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
