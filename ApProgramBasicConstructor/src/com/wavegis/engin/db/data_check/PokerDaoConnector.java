package com.wavegis.engin.db.data_check;

import java.util.List;

import com.wavegis.model.CheckDBData;

/** 需在指定的DB中新增一個"Demo"的DB */
public interface PokerDaoConnector {

	public List<CheckDBData> getData();

}
