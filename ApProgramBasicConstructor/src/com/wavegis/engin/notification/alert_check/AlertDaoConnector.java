package com.wavegis.engin.notification.alert_check;

import java.util.List;

import com.wavegis.model.SMSAlertData;

public interface AlertDaoConnector {

	public List<SMSAlertData> getData();

}
