package com.wavegis.engin.db.insert.flood;

import java.util.List;

import com.wavegis.model.flood.FloodLogData;

public interface FloodKMLInsertDaoConnector {
	public void insertFloodLogDatas(List<FloodLogData> floodLogDatas);
}
