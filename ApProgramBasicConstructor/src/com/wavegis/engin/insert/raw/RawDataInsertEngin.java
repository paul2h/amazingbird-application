package com.wavegis.engin.insert.raw;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class RawDataInsertEngin extends TimerEngin {

	private static final String enginID = "RawDataInsert";
	private static final String enginName = "水位資料寫入Engin";
	private static final RawDataInsertEnginView enginView = new RawDataInsertEnginView();
	private static final RawDao dao = RawDao.getInstance();

	private Logger logger;

	public RawDataInsertEngin() {
		setTimeout(GlobalConfig.CONFPIG_PROPERTIES.getProperty("INSERT_Time_Period"));
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
		List<WaterData> waterDatas = new ArrayList<>();
		WaterData waterData;
		while ((waterData = ProxyData.WATER_INSERT_QUEUE.poll()) != null) {
			showMessage("放入準備INSERT清單 : " + waterData.getStname() + " " + waterData.getLasttime());
			waterDatas.add(waterData);
		}
		
		if (waterDatas.size() > 0) {
			showMessage("寫入資料中...");
			dao.insertRawData(waterDatas);
			showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
		} else {
			showMessage("目前無需寫入資料.");
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
