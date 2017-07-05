package com.wavegis.engin.db.fake.gps_insert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.gps.GpsData;

public class FakeGpsCarInsertEngin implements Engin {
	// FIXME 最後整個Engin要拆掉 > 拆成GpsCSVDataReadEngin 跟 GpsDataInsertEngin
	public static final String enginID = "FakeCarInsert";
	private static final String enginName = "車機log假資料寫入(未完善)";
	private static final EnginView enginView = new FakeGpsCarInsertEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private boolean started = false;


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
	public boolean startEngin() {
		boolean success = false;
		readAndInsertProcess();
		started = true;
		success = true;
		return success;
	}
	
	private void readAndInsertProcess(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String dirPath = "D://temp//";//XXX
				List<String> csvList = scanCSVFiles(dirPath);
				for(String csvPath : csvList){
					showMessage("讀取檔案 : " + csvPath);
					List<GpsData> gpsDatas = readCSV(csvPath);
					showMessage("共 " + gpsDatas.size() + "筆資料.");
					
					showMessage("開始寫入...");
					insertGpsData(gpsDatas);
					showMessage(gpsDatas.size()+"筆資料  寫入完成.");
				}
			}
		}).start();
	}
	
	/** 確認所指定的資料夾中的log檔案數目 */
	private List<String> scanCSVFiles(String dirPath) {
		List<String> logFiles = new ArrayList<String>();
		logFiles.clear();
		File dir = new File(dirPath);
		for (String fileName : dir.list()) {
			showMessage("偵測到檔案 : " + fileName);
			logFiles.add(dirPath + fileName);// key取檔案的日期(數字)部分
		}
		return logFiles;
	}
	/**讀取CSV檔 格式範例 : 嘉義縣-01,0120000001,2017-07-05,5,17  (抽水機編號,uid,日期,開始小時,結束小時)*/
	private List<GpsData> readCSV(String csvFilePath){
		List<GpsData> result = new ArrayList<>();


		try {
			File file = new File(csvFilePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String readString = "";
			while (reader.ready()) {
				readString = reader.readLine();
				String[] gpsParameters = readString.split(",");
				if(gpsParameters.length == 5 && gpsParameters[1].length() == 10){//簡單防錯
					String uid = gpsParameters[1];
					Calendar dataTimeCalendar = Calendar.getInstance();
					dataTimeCalendar.setTime(simpleDateFormat.parse(gpsParameters[2]));
					dataTimeCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(gpsParameters[3]));
					int startHour = Integer.valueOf(gpsParameters[3]);
					int lastHour = Integer.valueOf(gpsParameters[4]);
					int hour = startHour;
					while(hour <= lastHour){
						for(int sec = 0 ; sec < 3600 ; sec+=30){
							dataTimeCalendar.add(Calendar.SECOND, 30);
							
							GpsData gpsData = new GpsData();
							gpsData.set_uid(uid);
							gpsData.setDatatime(dataTimeCalendar.getTime());
							gpsData.setOriginalMessage("FAKE-DATA");//XXX
							gpsData.setEvent("90");//XXX
							gpsData.setInput("00");//XXX
							gpsData.setStatus("A");//XXX
							
							result.add(gpsData);
							
						}
						hour++;
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return result;
	}
	
	private void insertGpsData(List<GpsData> gpsDatas){
		List<GpsData> tempList = new ArrayList<GpsData>();
		int dataCount = 0;
		for(GpsData gpsData : gpsDatas){
			showMessage("待寫入資料放入清單 : " + gpsData.get_uid() + " " + gpsData.getDatatime());
			tempList.add(gpsData);
			dataCount++;
			if(dataCount > 100){
				showMessage("開始寫入...");
				FakeGpsInsertDao.getInstance().insertGpsData(tempList);
				dataCount = 0;
				tempList.clear();
			}
		}
		
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean stopEngin() {
		return false;
	}
	
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}



}
