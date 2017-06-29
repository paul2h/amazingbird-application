package com.wavegis.engin.db.insert.water;

import java.util.List;

import com.wavegis.model.water.WaterData;

public interface WaterDaoConnector {
	public void insertWaterData(List<WaterData> waterData);
	
	public void insertRainData(List<WaterData> waterData);

}
