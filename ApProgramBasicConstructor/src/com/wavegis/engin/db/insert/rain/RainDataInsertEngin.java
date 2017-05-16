package com.wavegis.engin.db.insert.rain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class RainDataInsertEngin extends TimerEngin {

	private static final String enginID = "RainDataInsert";
	private static final String enginName = "雨量資料寫入Engin";
	private static final RainDataInsertEnginView enginView = new RainDataInsertEnginView();
	private static final RainDao dao = RainDao.getInstance();

	private Logger logger;

	public RainDataInsertEngin() {
		setTimeout(1000 * 30);
		logger = LogTool.getLogger(RainDataInsertEngin.class.getName());
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
		waterData.setRainfall10min(0);
		waterData.setStid("07_JIXIQ");
		waterData.setStname("錦孝橋F");
		List<WaterData> waterDatas = new ArrayList<>();
		waterDatas.add(waterData);
		showMessage("寫入資料中...");
		dao.insertRainData(waterDatas);
		showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
