package com.wavegis.engin.warn.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.SMSAlertData;

public class SMSSendEngin implements Engin {

	private static final ConcurrentHashMap<String, SMSAlertData> SMS_SEND_LIST = new ConcurrentHashMap<>();

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
				for (String stid : SMS_SEND_LIST.keySet()) {
					alertData = SMS_SEND_LIST.get(stid);
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
			}
		};
		new Timer().schedule(timerTask, 1000, 1000);
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
		String account = "11562";// TODO
		String password = "11562";// TODO
		String message = String.format("%s\n水位站:%s\n最新資料時間:%s\n最新水位:%s\n警戒設定:%s", alertData.getMessage(), alertData.getStnm(), sdf.format(alertData.getDatatime()), "" + alertData.getWaterlv(), "" + alertData.getAlert_value());
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
		}
		showMessage("傳送後回應訊息  : " + allResponseMessage);
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

	@Override
	public EnginView getEnginView() {
		return enginView;
	}
}
