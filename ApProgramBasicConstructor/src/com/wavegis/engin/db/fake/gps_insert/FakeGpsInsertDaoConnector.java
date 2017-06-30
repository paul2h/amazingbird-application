package com.wavegis.engin.db.fake.gps_insert;

import java.util.List;

import com.wavegis.model.gps.GpsData;

public interface FakeGpsInsertDaoConnector {
	public void insertGpsData( List<GpsData> gpsDatas);
}
