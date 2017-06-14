package com.wavegis.engin.db.read_conf;

import java.util.List;

import com.wavegis.model.CCTVData;

public interface DBConfigDaoConnector {

	public List<CCTVData> getCCTVData();
}
