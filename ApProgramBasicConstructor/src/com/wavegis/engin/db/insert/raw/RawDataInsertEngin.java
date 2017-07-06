package com.wavegis.engin.db.insert.raw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.WaterData;

public class RawDataInsertEngin extends TimerEngin {

	public static final String enginID = "RawDataInsert";
	private static final String enginName = "水位資料寫入1.0";
	private static final RawDataInsertEnginView enginView = new RawDataInsertEnginView();
	private static final RawDao dao = RawDao.getInstance();
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		showMessage("寫入資料中...");
		List<WaterData> waterDatas = new ArrayList<>();
		WaterData waterData;
		while((waterData = ProxyData.RAW_DATA_INSERT_QUEUE.poll())!= null){
			showMessage("放入待INSERT清單中 : " + waterData.getStid() + " " + simpleDateFormat.format(waterData.getLasttime()));
			waterDatas.add(waterData);
		}
		dao.insertRawData(waterDatas);
		showMessage(String.format("寫入完成,共%d筆", waterDatas.size()));
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
