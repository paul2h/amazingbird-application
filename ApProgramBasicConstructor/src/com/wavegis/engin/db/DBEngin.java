package com.wavegis.engin.db;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class DBEngin extends TimerEngin{

	private static final String enginID = "DB";
	private static final String enginName = "DB讀取Engin";
	private static final DBEnginView enginView = new DBEnginView();
	
	private DBDao dao;
	private Logger logger;

	public DBEngin(){
		logger = LogTool.getLogger(DBEngin.class.getName());
		dao = new DBDao();
		
		setTimeout(GlobalConfig.TimerPeriod_DB);
	}
	
	@Override
	public String getEnginID(){
		return enginID;
	}

	@Override
	public String getEnginName(){
		return enginName;
	}

	@Override
	public EnginView getEnginView(){
		return enginView;
	}

	@Override
	public void timerAction(){
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得資料...");
		
		List<CCTVData> cctvDataList = dao.getCCTVData();
		
		GlobalConfig.CCTV_DATA_LIST.clear();
		
		for(CCTVData cctvData : cctvDataList){
			GlobalConfig.CCTV_DATA_LIST.add(cctvData);
		}
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 資料取得結束.");
	}

	@Override
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}

}
