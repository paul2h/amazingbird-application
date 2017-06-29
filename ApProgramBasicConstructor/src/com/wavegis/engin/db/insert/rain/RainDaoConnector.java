package com.wavegis.engin.db.insert.rain;

import java.util.List;

import com.wavegis.model.water.WaterData;

public interface RainDaoConnector {

	public void insertRainData(List<WaterData> waterData);

}
