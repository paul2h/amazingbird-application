package com.wavegis.engin.db.read_conf;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.WaterData;

public class DBConfigEngin implements Engin {

	public static final String enginID = "DBConfig";
	private static final String enginName = "DB設定檔讀取Engin";
	private static final DBConfigEnginView enginView = new DBConfigEnginView();

	private DBConfigDao dao;
	private Logger logger;

	private boolean GET_CCTV_DATA = false;
	private boolean GET_RIVER_BASIN_DATA = false;
	private boolean GET_WEB_MONITOR_DATA = false;

	public DBConfigEngin() {
		logger = LogTool.getLogger(DBConfigEngin.class.getName());
		dao = DBConfigDao.getInstance();
		GET_CCTV_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_CCTV_DATA", "false"));
		GET_RIVER_BASIN_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_RIVER_BASIN_DATA", "false"));
		GET_WEB_MONITOR_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_WEB_MONITOR_DATA", "false"));
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

		if (GET_CCTV_DATA) {
			List<CCTVData> cctvDataList;
			try {
				cctvDataList = dao.getCCTVData();
				GlobalConfig.CCTV_DATA_LIST.clear();
				for (CCTVData cctvData : cctvDataList) {
					showMessage("讀取CCTV資料 : " + cctvData.getStname());
					GlobalConfig.CCTV_DATA_LIST.add(cctvData);
				}
				showMessage(GlobalConfig.dateFormat.format(new Date()) + " CCTV資料取得結束.");
			} catch (Exception e) {
				showMessage("CCTV資料取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		if (GET_RIVER_BASIN_DATA) {
			showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得河床底高資料...");

			List<WaterData> bottomDatas;
			try {
				bottomDatas = dao.getRiverBottomDatas();
				GlobalConfig.RIVER_BOTTOM_DATAS.clear();
				for (WaterData bottomData : bottomDatas) {
					GlobalConfig.RIVER_BOTTOM_DATAS.put(bottomData.getStid().trim(), bottomData);
					showMessage("讀取完成  : " + bottomData.getStname() + " 底高 : " + bottomData.getBottom_height());
				}
				showMessage(GlobalConfig.dateFormat.format(new Date()) + " 河床底高資料取得結束.");
			} catch (Exception e) {
				showMessage("河床資料取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		if(GET_WEB_MONITOR_DATA){
			showMessage(GlobalConfig.dateFormat.format(new Date()) + " 開始取得網頁監控清單資料...");
			List<WebMonitorFocusData> web_datas;
			try {
				web_datas = dao.getWebFocusDatas();
			
				for(WebMonitorFocusData focusData : web_datas){
					showMessage("取得資料 : " + focusData.getProject_name() + " " + focusData.getWeb_url());
					GlobalConfig.WEB_MONITOR_URL_LIST.add(focusData);
				}
				showMessage(GlobalConfig.dateFormat.format(new Date()) + " 網頁監控清單資料取得結束.");
			} catch (Exception e) {
				showMessage("網頁監控清單取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}
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
