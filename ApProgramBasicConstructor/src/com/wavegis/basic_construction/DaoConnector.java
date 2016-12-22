package com.wavegis.basic_construction;

/** 需在指定的DB中新增一個"Demo"的DB */
public interface DaoConnector {

	public void creatDemoTable();

	public void insertData();

}
