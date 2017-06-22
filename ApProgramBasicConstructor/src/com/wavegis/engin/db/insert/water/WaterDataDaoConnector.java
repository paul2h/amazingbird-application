package com.wavegis.engin.db.insert.water;

import java.util.List;

import com.wavegis.model.RainData;
import com.wavegis.model.WaterData;

public interface WaterDataDaoConnector {
	public int insertRainData(List<RainData> rainData);
	
	public int insertWaterData(List<WaterData> waterData);
}
