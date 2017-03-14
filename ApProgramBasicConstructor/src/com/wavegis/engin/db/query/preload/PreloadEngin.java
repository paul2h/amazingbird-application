package com.wavegis.engin.db.query.preload;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.PreloadData;

public class PreloadEngin implements Engin {

	private static final String enginID = "DBPreload";
	private static final String enginName = "DB預載資料";
	private static final PreloadEnginView enginView = new PreloadEnginView();

	private PreloadDao dao;
	private Logger logger;

	public PreloadEngin() {
		logger = LogTool.getLogger(PreloadEngin.class.getName());
		dao = new PreloadDao();
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

		List<PreloadData> preloadDataList = dao.getRawPreloadData();

		ProxyData.ENGIN_PRELOAD_MAP.clear();

		for(PreloadData preloadData : preloadDataList){
			showMessage(preloadData.getStno() + ", " + preloadData.getTableName());
			ProxyData.ENGIN_PRELOAD_MAP.put(preloadData.getStno(), preloadData.getTableName());
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
