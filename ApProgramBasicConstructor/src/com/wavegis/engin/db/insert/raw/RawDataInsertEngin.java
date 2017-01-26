package com.wavegis.engin.db.insert.raw;

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
		List<WaterData> locatorDataList = new ArrayList<WaterData>();
		List<WaterData> procalDataList = new ArrayList<WaterData>();
		WaterData waterData;
		while((waterData = ProxyData.WATER_INSERT_QUEUE.poll()) != null){
			String mapKey = waterData.getStid();
			String tableName = String.valueOf(ProxyData.ENGIN_PRELOAD_MAP.get(mapKey));
			
			if(tableName == null){
				continue;
			} else if("level_raw_station".equals(tableName)){
				locatorDataList.add(waterData);
			} else if("level_raw_station1".equals(tableName)){
				procalDataList.add(waterData);
			}
			//showMessage("放入準備INSERT清單 : " + waterData.getStname() + " " + waterData.getLasttime());
		}
		
		if(locatorDataList.size() > 0 || procalDataList.size() > 0){
			int count = 0;
			
			showMessage("寫入資料中...");
			
			if(locatorDataList.size() > 0){
				count += locatorDataList.size();
				
				dao.insertRawLocatorData(locatorDataList);
			}
			if(procalDataList.size() > 0){
				count += procalDataList.size();
				
				dao.insertRawProcalData(procalDataList);
			}
			showMessage(String.format("寫入完成,共%d筆", count));
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
