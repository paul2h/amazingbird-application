package com.wavegis.engin.db.read_conf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.water.WaterData;

public class DBConfigEngin implements Engin {

	public static final String enginID = "DBConfig";
	private static final String enginName = "DB設定檔讀取1.2";
	private static final DBConfigEnginView enginView = new DBConfigEnginView();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private DBConfigDao dao;
	private Logger logger;

	private boolean GET_CCTV_DATA = false;
	private boolean GET_RIVER_BASIN_DATA = false;
	private boolean GET_WEB_MONITOR_DATA = false;
	private boolean GET_KENKUL_ID_SETTING_DATA = false;

	public DBConfigEngin() {
		logger = LogTool.getLogger(DBConfigEngin.class.getName());
		GET_CCTV_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_CCTV_DATA", "false"));
		GET_RIVER_BASIN_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_RIVER_BASIN_DATA", "false"));
		GET_WEB_MONITOR_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_WEB_MONITOR_DATA", "false"));
		GET_KENKUL_ID_SETTING_DATA = Boolean.valueOf(GlobalConfig.XML_CONFIG.getProperty("GET_KENKUL_ID_SETTING_DATA", "false"));
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
		if (dao == null) {
			showMessage("取得DAO...");
			dao = DBConfigDao.getInstance();
		}

		showMessage(dateFormat.format(new Date()) + " 開始取得CCTV資料...");

		if (GET_CCTV_DATA) {
			List<CCTVData> cctvDataList;
			try {
				cctvDataList = dao.getCCTVData();
				ProxyData.CCTV_DATA_LIST.clear();
				for (CCTVData cctvData : cctvDataList) {
					showMessage("讀取CCTV資料 : " + cctvData.getStname());
					ProxyData.CCTV_DATA_LIST.add(cctvData);
				}
				showMessage(dateFormat.format(new Date()) + " CCTV資料取得結束.");
			} catch (Exception e) {
				showMessage("CCTV資料取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}

		if (GET_RIVER_BASIN_DATA) {
			showMessage(dateFormat.format(new Date()) + " 開始取得河床底高資料...");

			List<WaterData> bottomDatas;
			try {
				bottomDatas = dao.getRiverBottomDatas();
				ProxyData.RIVER_BOTTOM_DATAS.clear();
				for (WaterData bottomData : bottomDatas) {
					ProxyData.RIVER_BOTTOM_DATAS.put(bottomData.getStid().trim(), bottomData);
					showMessage("讀取完成  : " + bottomData.getStname() + " 底高 : " + bottomData.getBottom_height());
				}
				showMessage(dateFormat.format(new Date()) + " 河床底高資料取得結束.");
			} catch (Exception e) {
				showMessage("河床資料取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}

		if (GET_WEB_MONITOR_DATA) {
			showMessage(dateFormat.format(new Date()) + " 開始取得網頁監控清單資料...");

			List<WebMonitorFocusData> web_datas;
			try {
				ProxyData.WEB_MONITOR_URL_LIST.clear();
				web_datas = dao.getWebFocusDatas();
				for (WebMonitorFocusData focusData : web_datas) {
					showMessage("取得資料 : " + focusData.getProject_name() + " " + focusData.getWeb_url());
					ProxyData.WEB_MONITOR_URL_LIST.add(focusData);
				}
				showMessage(dateFormat.format(new Date()) + " 網頁監控清單資料取得結束.");
			} catch (Exception e) {
				showMessage("網頁監控清單取得錯誤 : " + e.getMessage());
				e.printStackTrace();
			}
		}

		if (GET_KENKUL_ID_SETTING_DATA) {
			showMessage(dateFormat.format(new Date()) + " 開始取得塏固Gateway設定資料...");
			ProxyData.KENKUL_GATEWAY_ID_MAP.clear();
			dao.getKenkulGatewayIDMap().forEach(kenkulGatewayMapping -> {
				showMessage("取得GatewayID對照資料 : " + kenkulGatewayMapping.getGateway_id() + " >> " + kenkulGatewayMapping.getRepresent_gateway_id());
				ProxyData.KENKUL_GATEWAY_ID_MAP.put(kenkulGatewayMapping.getGateway_id(), kenkulGatewayMapping.getRepresent_gateway_id());
			});
			showMessage(dateFormat.format(new Date()) + " 塏固Gateway設定資料取得結束.");
		}
		return true;
	}

	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	@Override
	public boolean isStarted() {
		return false;// 此Engine無長久開著 不用判別是否開啟中
	}

	@Override
	public boolean stopEngin() {
		return false;
	}

}
