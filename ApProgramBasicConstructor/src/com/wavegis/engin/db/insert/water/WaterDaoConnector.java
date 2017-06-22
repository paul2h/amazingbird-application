package com.wavegis.engin.db.insert.water;

import java.util.List;

import com.wavegis.model.WaterData;

public interface WaterDaoConnector {
	public void insertWaterData(List<WaterData> waterData);
}
