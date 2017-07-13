package com.wavegis.engin.db.insert.rain;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.RainData;

public class RainDataInsertEngin extends TimerEngin {

	public static final String enginID = "RainDataInsert";
	private static final String enginName = "雨量資料寫入1.0";
	private static final RainDataInsertEnginView enginView = new RainDataInsertEnginView();
	private static RainDao dao = null;

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
		if(dao == null){
			showMessage("初始畫DAO...");
			dao = RainDao.getInstance();
		}
		List<RainData> rainDatas = new ArrayList<>();
		RainData rainData;
		while((rainData = ProxyData.RAIN_DATA_INSERT_QUEUE.poll()) != null){
			showMessage(String.format("放入待寫清單 : %s  %s "  , rainData.getStid() , rainData.getLasttime()));
			rainDatas.add(rainData);
		}
		showMessage("寫入資料中...");
		dao.insertRainData(rainDatas);
		showMessage(String.format("寫入完成,共%d筆", rainDatas.size()));
		rainDatas.clear();
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
