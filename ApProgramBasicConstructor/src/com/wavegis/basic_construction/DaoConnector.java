package com.wavegis.basic_construction;

import java.util.List;

import com.wavegis.model.DataModel;

/** 需在指定的DB中新增一個"Demo"的DB */
public interface DaoConnector {

	public void creatDemoTable();

	public void insertData();

	public List<DataModel> getData();

}
