package com.wavegis.global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.MailData;
import com.wavegis.model.RainData;
import com.wavegis.model.SMSAlertData;
import com.wavegis.model.WaterData;

public class ProxyData {
	public static final ConcurrentLinkedQueue<RainData> WATER_INSERT_RAIN_QUEUE = new ConcurrentLinkedQueue<RainData>();
	public static final ConcurrentLinkedQueue<WaterData> WATER_INSERT_WATER_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	public static final Map<String, Double> RIVER_BASINS = new ConcurrentHashMap<String, Double>();
	public static final Map<String, SMSAlertData> SMS_SEND_LIST = new ConcurrentHashMap<String, SMSAlertData>();
	public static final ConcurrentLinkedQueue<MailData> MAIL_SEND_QUEUE = new ConcurrentLinkedQueue<MailData>();
}
