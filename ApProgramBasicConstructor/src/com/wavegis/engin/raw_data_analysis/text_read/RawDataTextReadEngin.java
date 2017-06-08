package com.wavegis.engin.raw_data_analysis.text_read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.ProxyDatas;
import com.wavegis.global.tools.LogTool;

public class RawDataTextReadEngin implements Engin {

	public static final String enginID = "RawDataTextRead";
	private static final String enginName = "原始資料文字檔讀取1.0(未完成)";
	private static final EnginView enginView = new RawDataTextReadEnginView();
	private boolean started = false;
	private Logger logger = LogTool.getLogger(this.getClass().getName());

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
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean startEngin() {
		started = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				insertProcess();
				started = false;
			}
		}).start();
		return true;
	}

	@Override
	public boolean stopEngin() {
		started = false;
		return true;
	}

	String LogPath = "D:\\temp\\";// TODO

	private void insertProcess() {
		boolean success = false;

		List<String> logFileList;
		logFileList = scanLogDatas(LogPath);
		int count = 0;
		for (String logFile : logFileList) {
			showMessage("開始檔案讀取&寫入 : " + logFile);
			success = readTxtAndInsert(logFile);
			count++;
			if(count == 48){
				count = 0;
				try {
					Thread.sleep(1000*30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (!success) {
			showMessage("讀取寫入失敗");
		}
	}

	/** 確認所指定的資料夾中的log檔案數目 */
	private List<String> scanLogDatas(String dirPath) {
		List<String> logFiles = new ArrayList<String>();
		logFiles.clear();
		File dir = new File(dirPath);
		for (String fileName : dir.list()) {
			showMessage("偵測到檔案 : " + fileName);
			logFiles.add(LogPath + fileName);// key取檔案的日期(數字)部分
		}
		return logFiles;
	}

	private boolean readTxtAndInsert(String filePath) {
		boolean success = false;

		try {
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String readString = "";
			while (reader.ready()) {
				readString = reader.readLine();
				if (readString.indexOf("##KENKUL##") > 0) {// TODO
					String filteString = readString.substring(readString.indexOf("##KENKUL##")).replaceAll("##KENKUL##", "").trim();// TODO
					showMessage("取得資料,放入QUEUE中 : " + filteString);
					ProxyDatas.KENKUL_RAW_DATA.offer(filteString);
				}
				readString = "";
			}
			success = true;
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}

	private void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
