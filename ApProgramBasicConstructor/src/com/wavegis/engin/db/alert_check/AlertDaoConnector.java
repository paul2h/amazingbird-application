package com.wavegis.engin.db.alert_check;

import java.util.List;

import com.wavegis.model.flood.FloodAlertData;
import com.wavegis.model.water.WaterAlertData;

/** 需在指定的DB中新增一個"Demo"的DB */
public interface AlertDaoConnector {

	public List<WaterAlertData> getWaterlevelAlertData();
	
	public List<FloodAlertData> getFloodAlertData();

}
