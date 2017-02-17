package com.wavegis.engin.db.alert_check;

import java.util.List;

import com.wavegis.model.SMSAlertData;

/** 需在指定的DB中新增一個"Demo"的DB */
public interface AlertDaoConnector {

	public List<SMSAlertData> getData();

}
