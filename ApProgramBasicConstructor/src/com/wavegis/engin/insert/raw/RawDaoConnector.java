package com.wavegis.engin.insert.raw;

import java.util.List;

import com.wavegis.model.DataModel;
import com.wavegis.model.WaterData;

public interface RawDaoConnector {

	public void insertRawData(List<WaterData> waterData);

}
