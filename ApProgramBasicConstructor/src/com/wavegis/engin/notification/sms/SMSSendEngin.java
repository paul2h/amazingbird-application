package com.wavegis.engin.notification.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.SMSAlertData;

public class SMSSendEngin implements Engin {

	public static final String enginID = "sms";
	private static final String enginName = "簡訊發送Engin";
	private static final SMSSendEnginView enginView = new SMSSendEnginView();
	private boolean isStarted = false;
	private Logger logger;
	private TimerTask timerTask;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String SMS_SERVER_URL_PREFIX = GlobalConfig.XML_CONFIG.getProperty("SMS_SERVER_URL_PREFIX");
	private static String SMS_Account = GlobalConfig.XML_CONFIG.getProperty("SMS_Account");
	private static String SMS_Password = GlobalConfig.XML_CONFIG.getProperty("SMS_Password");
	private static final String encoding = "Big5";
	private static final String serverEncoding = "Big5";// Server回傳的編碼,此為固定值
	private static String SMS_SERVER_URL = String.format("%s?username=%s&password=%s&encoding=%s", SMS_SERVER_URL_PREFIX, SMS_Account, SMS_Password, encoding);
	private static final char enterCode = (char) 6;// ASCII Code

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public boolean startEngin() {
		showMessage("準備開啟Engin...");
		timerTask = new TimerTask() {
			@Override
			public void run() {
				showMessage("開始傳送警訊");
				SMSAlertData alertData;
				for (String stid : ProxyData.SMS_SEND_LIST.keySet()) {
					alertData = ProxyData.SMS_SEND_LIST.get(stid);
					if (!alertData.HasSend()) {
						showMessage("傳送警戒簡訊 : " + alertData.getStnm());
						try {
							sendSMSProcess(alertData);
							alertData.setHasSend(true);// 傳送過了做記號
						} catch (IOException e) {
							showMessage("傳送警訊失敗 : " + alertData.getStnm());
							e.printStackTrace();
						}
					}
				}
				showMessage("傳送警訊結束");
				checkSend();
			}
		};
		new Timer().schedule(timerTask, 1000, Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_SMS")));
		isStarted = true;
		showMessage("Engin已啟動");
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("準備停止Engin");
		boolean stopSuccess = timerTask.cancel();
		if (stopSuccess) {
			isStarted = false;
			timerTask = null;
			showMessage("Engin已停止");
		} else {
			showMessage("停止失敗");
		}
		return stopSuccess;
	}

	private boolean sendSMSProcess(SMSAlertData alertData) throws IOException {
		boolean isSuccess = false;

		String[] phoneNumbers = alertData.getPhones().replaceAll("\\{", "").replaceAll("\\}", "").split(",");

		// 第一封
		String message = String.format("水位站警訊訊息:%s(%s)%s%s%s", "" + enterCode, alertData.getStnm(), "" + enterCode, alertData.getMessage(), "" + enterCode);
		String responseMessage = send(phoneNumbers, message);// TODO 回傳成功確認
		// 第二封
		message = String.format("水位站警界資訊:%s(%s)%s最新資料時間:%s%s最新水位:%s%s警戒水位:%s", "" + enterCode, alertData.getStnm(), enterCode, sdf.format(alertData.getDatatime()), enterCode, "" + alertData.getWaterlv(), enterCode, "" + alertData.getAlert_value());
		responseMessage += send(phoneNumbers, message);// TODO 回傳成功確認

		System.out.println(responseMessage);
		return isSuccess;
	}

	private String send(String[] phoneNumbers, String message) throws IOException {
		HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(SMS_SERVER_URL).openConnection();
		httpURLConnection.setInstanceFollowRedirects(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");

		httpURLConnection.setRequestProperty("Accept", "*/*");
		httpURLConnection.setRequestProperty("Accept-Language", "zh-tw");
		httpURLConnection.setRequestProperty("Content-Type", "text/html");
		httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");

		DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
		for (int i = 0; i < phoneNumbers.length; i++) {
			String phoneNumber = phoneNumbers[i];
			dataOutputStream.write(String.format("[%d]\r\n", i).getBytes(encoding));// 編號
			dataOutputStream.write(String.format("dstaddr=%s\r\n", phoneNumber).getBytes(encoding));
			dataOutputStream.write(String.format("smbody=%s\r\n", message).getBytes(encoding));
		}

		dataOutputStream.flush();
		dataOutputStream.close();

		String responseMessage = "\n";
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), serverEncoding));
			String line;

			while ((line = bufferReader.readLine()) != null) {
				responseMessage += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (Exception e) {
			} finally {
				bufferReader = null;
			}
		}

		try {
			dataOutputStream.close();
		} catch (Exception e) {
		} finally {
			dataOutputStream = null;
		}

		httpURLConnection = null;

		return responseMessage;
	}

	private void showMessage(String message) {
		enginView.showMessage(message);

		if (logger == null) {
			initLogger();
		}
		logger.info(message);
	}

	private void initLogger() {
		logger = LogTool.getLogger(SMSSendEngin.class.getName());
		showMessage("logger初始化完成");
	}

	/** 每日12點清一次傳送清單 */
	private void checkSend() {
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 12) {
			showMessage("清空原本清單");
			ProxyData.SMS_SEND_LIST.clear();
		}
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}
}
