package com.wavegis.engin.notification.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
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
						try {
							sendSMS(alertData);
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

	private boolean sendSMS(SMSAlertData alertData) throws IOException {
		boolean success = false;
		showMessage("send alert " + alertData.getStid());
		// #[[ 第一封
		showMessage("傳送警界簡訊第一封(共2封)...");
		String account = GlobalConfig.XML_CONFIG.getProperty("SMS_Account");
		String password = GlobalConfig.XML_CONFIG.getProperty("SMS_Password");
		String message = String.format("水位站警訊訊息:(%s)\n%s\n", alertData.getStnm(), alertData.getMessage());
		String urlString = "http://imsp.emome.net:8008/imsp/sms/servlet/SubmitSM?account=" + account
				+ "&password=" + password
				+ "&from_addr_type=0&from_addr=&to_addr_type=0&to_addr=" + alertData.getPhones().replaceAll("\\{", "").replaceAll("\\}", "")
				+ "&msg_expire_time=0&msg_type=0&msg=" + StringToHexString(message)
				+ "&dest_port=c350";
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String allResponseMessage = "";
		String line;
		while ((line = in.readLine()) != null) {
			allResponseMessage += line;
		}
		if (allResponseMessage.indexOf("Success") > 0) {
			// 只要有一通成功就算成功
			success = true;
		} else {
			success = false;
		}
		showMessage("傳送後回應訊息  : " + allResponseMessage);
		showMessage("傳送警界簡訊第一封結束.");
		// ]]
		// #[[ 第二封
		showMessage("傳送警界簡訊第二封(共2封)...");
		message = String.format("水位站警界資訊:(%s)\n最新資料時間:%s\n最新水位:%s\n警戒水位:%s", alertData.getStnm(), sdf.format(alertData.getDatatime()), "" + alertData.getWaterlv(), "" + alertData.getAlert_value());
		urlString = "http://imsp.emome.net:8008/imsp/sms/servlet/SubmitSM?account=" + account
				+ "&password=" + password
				+ "&from_addr_type=0&from_addr=&to_addr_type=0&to_addr=" + alertData.getPhones().replaceAll("\\{", "").replaceAll("\\}", "")
				+ "&msg_expire_time=0&msg_type=0&msg=" + StringToHexString(message)
				+ "&dest_port=c350";
		url = new URL(urlString);
		connection = url.openConnection();
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		allResponseMessage = "";
		while ((line = in.readLine()) != null) {
			allResponseMessage += line;
		}
		if (allResponseMessage.indexOf("Success") > 0) {
			// 只要有一通成功就算成功
			success = true;
		} else {
			success = false;
		}
		showMessage("傳送後回應訊息  : " + allResponseMessage);
		showMessage("傳送警界簡訊第二封結束.");
		// ]]

		return success;
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

	/** 把訊息從String轉成簡訊要的格式 (16進位)方法待研究 */
	private String StringToHexString(String message) throws UnsupportedEncodingException {
		byte[] in = message.getBytes("Big5");
		byte ch = 0x00;
		int i = 0;

		if (in == null || in.length <= 0)
			return null;
		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			out.append("%");
			ch = (byte) (in[i] & 0xF0);
			ch = (byte) (ch >>> 4);
			ch = (byte) (ch & 0x0F);
			out.append(pseudo[(int) ch]);
			ch = (byte) (in[i] & 0x0F);
			out.append(pseudo[(int) ch]);
			i++;
		}
		return new String(out);
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
