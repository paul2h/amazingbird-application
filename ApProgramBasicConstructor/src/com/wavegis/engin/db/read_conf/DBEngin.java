package com.wavegis.engin.db.read_conf;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class DBEngin implements Engin {

	private static final String enginID = "DB";
	private static final String enginName = "DB讀取Engin";
	private static final DBEnginView enginView = new DBEnginView();

	
	private DBDao dao;
	private Logger logger;

	public DBEngin() {
		logger = LogTool.getLogger(DBEngin.class.getName());
		dao = new DBDao();
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
	public boolean startEngin() {
		showMessage("開始取得資料...");

		List<CCTVData> cctvDataList = dao.getCCTVData();

		
		GlobalConfig.CCTV_DATA_LIST.clear();

		for (CCTVData cctvData : cctvDataList) {
			showMessage("放入資料 : " + cctvData.getStname());
			GlobalConfig.CCTV_DATA_LIST.add(cctvData);
		}
		showMessage("資料取得結束.");

		return true;
	}

	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public boolean stopEngin() {
		return false;
	}
}
