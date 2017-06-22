package com.wavegis.engin.db.insert.raw;

import java.util.List;

import com.wavegis.model.WaterData;

public interface RawDaoConnector {

	public void insertRawLocatorData(List<WaterData> waterData);
	
	public void insertRawProcalData(List<WaterData> waterData);
	
	public void insertRawData(List<WaterData> waterData);

}
