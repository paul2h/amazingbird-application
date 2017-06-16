package com.wavegis.engin.db.read_conf;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;
import com.wavegis.model.WaterData;

public class DBConfigEngin implements Engin {

	private static final String enginID = "DBConfig";
	private static final String enginName = "DB設定檔讀取Engin";
	private static final DBConfigEnginView enginView = new DBConfigEnginView();

	private DBConfigDao dao;
	private Logger logger;

	public DBConfigEngin() {
		logger = LogTool.getLogger(DBConfigEngin.class.getName());
		dao = new DBConfigDao();
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
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得CCTV資料...");

		List<CCTVData> cctvDataList = dao.getCCTVData();

		GlobalConfig.CCTV_DATA_LIST.clear();

		for (CCTVData cctvData : cctvDataList) {
			showMessage("讀取CCTV資料 : " + cctvData.getStname());
			GlobalConfig.CCTV_DATA_LIST.add(cctvData);
		}
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " CCTV資料取得結束.");

		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得河床底高資料...");

		List<WaterData> bottomDatas = dao.getRiverBottomDatas();

		GlobalConfig.RIVER_BOTTOM_DATAS.clear();

		for (WaterData bottomData : bottomDatas) {
			GlobalConfig.RIVER_BOTTOM_DATAS.put(bottomData.getStid().trim(), bottomData);
			showMessage("讀取完成  : " + bottomData.getStname() + " 底高 : " + bottomData.getBottom_height());
		}
		showMessage(GlobalConfig.dateFormat.format(new Date()) + " 河床底高資料取得結束.");

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
