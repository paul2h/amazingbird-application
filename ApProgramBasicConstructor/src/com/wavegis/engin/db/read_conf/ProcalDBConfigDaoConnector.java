package com.wavegis.engin.db.read_conf;

import java.util.List;

import com.wavegis.model.water.KenkulGatewayIDMapping;
import com.wavegis.model.water.WaterData;

public interface ProcalDBConfigDaoConnector {

	public List<WaterData> getStationBottomDatas();
	
	public List<KenkulGatewayIDMapping> getKenkulGatewayIDMap();
}
