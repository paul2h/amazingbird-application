package com.wavegis.global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wavegis.model.SMSAlertData;

public class ProxyData {

	public static final Map<String, SMSAlertData> SMS_SEND_LIST = new ConcurrentHashMap<>();
}
