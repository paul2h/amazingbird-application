package com.wavegis.engin.db.select;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class DBConfigEngin implements Engin{

	private static final String enginID = "DBConfig";
	private static final String enginName = "DB設定檔讀取Engin";
	private static final DBConfigEnginView enginView = new DBConfigEnginView();
	
	private DBConfigDao dao;
	private Logger logger;

	public DBConfigEngin(){
		logger = LogTool.getLogger(DBConfigEngin.class.getName());
		dao = new DBConfigDao();
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
	public boolean startEngin(){
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得資料...");
		
		List<CCTVData> cctvDataList = dao.getCCTVData();
		
		GlobalConfig.CCTV_DATA_LIST.clear();
		
		for(CCTVData cctvData : cctvDataList){
			GlobalConfig.CCTV_DATA_LIST.add(cctvData);
		}
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 資料取得結束.");
		
		return true;
	}

	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}

	@Override
	public boolean isStarted(){
		return false;
	}

	@Override
	public boolean stopEngin(){
		return false;
	}

}
