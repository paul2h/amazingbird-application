package com.wavegis.engin.db.insert.water;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.WaterData;

public class WaterDataInsertEngin extends TimerEngin {

	public static final String enginID = "WaterDataInsert";
	private static final String enginName = "水情資料寫入2.0";
	private static final WaterDataInsertEnginView enginView = new WaterDataInsertEnginView();
	private static WaterDao dao = null;

	private Logger logger;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WaterDataInsertEngin() {
		setTimeout(1000 * 30);
		logger = LogTool.getLogger(WaterDataInsertEngin.class.getName());
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
	public void timerAction() {
		if(dao == null){
			showMessage("初始化DAO...");
			dao = WaterDao.getInstance();
		}
		List<WaterData> waterDatas = new ArrayList<>();
		WaterData waterData;
		while ((waterData = ProxyData.WATER_DATA_INSERT_QUEUE.poll()) != null) {
			showMessage("從INSERT QUEUE取得資料 : " + waterData.getStid() + " " + simpleDateFormat.format(waterData.getLasttime()));
			waterDatas.add(waterData);
		}
		if (waterDatas.size() > 0) {
			showMessage("寫入資料中...");
			dao.insertWaterData(waterDatas);
			dao.insertRainData(waterDatas);
			showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
		}
		waterDatas.clear();
		waterDatas = null;

	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
