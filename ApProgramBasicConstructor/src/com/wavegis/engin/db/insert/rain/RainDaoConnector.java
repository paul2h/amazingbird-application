package com.wavegis.engin.db.insert.rain;

import java.util.List;

import com.wavegis.model.RainData;

public interface RainDaoConnector {

	public void insertRainData(List<RainData> rainData);

}
