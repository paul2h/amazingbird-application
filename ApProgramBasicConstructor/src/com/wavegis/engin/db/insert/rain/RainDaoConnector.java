package com.wavegis.engin.db.insert.rain;

import java.util.List;

import com.wavegis.model.DataModel;
import com.wavegis.model.WaterData;

public interface RainDaoConnector {

	public void insertRainData(List<WaterData> waterData);

}
