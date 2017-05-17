package com.wavegis.engin.db.insert.water;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyDatas;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.OriginalWaterData;
import com.wavegis.model.water.WaterData;

public class WaterDataInsertEngin extends TimerEngin {

	public static final String enginID = "WaterDataInsert";
	private static final String enginName = "水情資料寫入Engin";
	private static final WaterDataInsertEnginView enginView = new WaterDataInsertEnginView();
	private static final WaterDao dao = WaterDao.getInstance();

	private Logger logger;

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
		List<WaterData> waterDatas = new ArrayList<>();
		WaterData waterData;
		OriginalWaterData<Double> originalWaterData;
		while ((originalWaterData = ProxyDatas.WATER_DATA_INSERT_QUEUE.poll()) != null) {
			waterData = new WaterData();
			waterData.setLasttime(originalWaterData.getDatatime());
			waterData.setStid(originalWaterData.getStid());

			Double[] datas = originalWaterData.getDatas();
			if (datas.length >= 12) {// TODO 目前針對塏固 需要再行修正成多種可用的運算方式
				waterData.setWaterlevel(datas[0] * 0.0001 * 4 * 0.0004);
				waterData.setRainfallCounter(datas[2]);
				waterData.setVoltage(datas[4]);
				waterData.setRainfall10min(datas[6]);
				waterData.setRainfall1hour(datas[7]);
				waterData.setRainfall3hour(datas[8]);
				waterData.setRainfall6hour(datas[9]);
				waterData.setRainfall12hour(datas[10]);
				waterData.setRainfall24hour(datas[11]);
				
				waterDatas.add(waterData);
			}
		}
		showMessage("寫入資料中...");
		dao.insertWaterData(waterDatas);
		dao.insertRainData(waterDatas);
		showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
