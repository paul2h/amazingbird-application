package com.wavegis.engin.db.insert.water;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.RainData;
import com.wavegis.model.WaterData;

public class WaterDataInsertEngin extends TimerEngin {

	private static final String enginID = "WaterDataInsert";
	private static final String enginName = "水位資料寫入Engin";
	private static final WaterDataInsertEnginView enginView = new WaterDataInsertEnginView();
	private static final WaterDataDao dao = WaterDataDao.getInstance();

	private Logger logger;

	public WaterDataInsertEngin(){
		logger = LogTool.getLogger(WaterDataInsertEngin.class.getName());
		
		setTimeout(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_DB", "600000"));
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
	public void timerAction(){
		List<RainData> rainDataList = new ArrayList<RainData>();
		List<WaterData> waterDataList = new ArrayList<WaterData>();
		
		if(!ProxyData.WATER_INSERT_RAIN_QUEUE.isEmpty()){
			RainData rainData;
			
			while((rainData = ProxyData.WATER_INSERT_RAIN_QUEUE.poll()) != null){
				if(rainData.getRain_current() >= 0){
					//showMessage("放入準備待寫入清單 : " + rainData.getStid() + ", " + rainData.getLasttime());
					
					if(rainData.getVoltage() == 0){
						rainData.setVoltage(15);
					}
					rainDataList.add(rainData);
				}
			}
		}
		if(!ProxyData.WATER_INSERT_WATER_QUEUE.isEmpty()){
			WaterData waterData;
			
			while((waterData = ProxyData.WATER_INSERT_WATER_QUEUE.poll()) != null){
				if(waterData.getWaterlevel() >= 0){
					//showMessage("放入準備待寫入清單 : " + waterData.getStid() + ", " + waterData.getLasttime());
					
					if(waterData.getVoltage() == 0){
						waterData.setVoltage(15);
					}
					waterDataList.add(waterData);
				}
			}
		}
		if(rainDataList.isEmpty() && waterDataList.isEmpty()){
			showMessage("目前無資料需寫入.");
			
			return ;
		}
		showMessage("寫入資料中...");
		
		if(rainDataList.size() > 0){
			dao.insertRainData(rainDataList);
		}
		if(waterDataList.size() > 0){
			dao.insertWaterData(waterDataList);
		}
		showMessage("寫入完畢.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
