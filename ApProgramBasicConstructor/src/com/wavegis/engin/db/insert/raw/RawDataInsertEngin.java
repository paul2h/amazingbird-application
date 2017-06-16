package com.wavegis.engin.db.insert.raw;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class RawDataInsertEngin extends TimerEngin {

	private static final String enginID = "RawDataInsert";
	private static final String enginName = "水位資料寫入Engin";
	private static final RawDataInsertEnginView enginView = new RawDataInsertEnginView();
	private static final RawDao dao = RawDao.getInstance();

	private Logger logger;

	public RawDataInsertEngin() {
		setTimeout(1000 * 30);
		logger = LogTool.getLogger(RawDataInsertEngin.class.getName());
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
		// TODO 這邊只放假資料 需要放入真實資料
		WaterData waterData = new WaterData();
		waterData.setLasttime(new Timestamp(System.currentTimeMillis()));
		waterData.setStid("07_JIXIQ");
		waterData.setStname("錦孝橋F");
		waterData.setWaterlevel(12.1);
		waterData.setVoltage(0);
		List<WaterData> waterDatas = new ArrayList<>();
		waterDatas.add(waterData);
		showMessage("寫入資料中...");
		dao.insertRawData(waterDatas);
		showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
