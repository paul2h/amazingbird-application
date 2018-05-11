package com.wavegis.engin.db.read_conf;

import java.util.List;

import com.wavegis.model.CCTVData;
import com.wavegis.model.WebMonitorFocusData;

public interface DBConfigDaoConnector {

	public List<CCTVData> getCCTVData();
	
	public List<WebMonitorFocusData> getWebFocusData();
	
}
