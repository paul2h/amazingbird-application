package com.wavegis.engin.db.query.preload;

import java.util.List;

import com.wavegis.model.PreloadData;

public interface PreloadDaoConnector {

	public List<PreloadData> getRawPreloadData();
}
