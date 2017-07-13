package com.wavegis.global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.MailData;
import com.wavegis.model.RainData;
import com.wavegis.model.SMSAlertData;
import com.wavegis.model.water.WaterData;

public class ProxyData {
	
	
	public static final Map<String, Double> RIVER_BASINS = new ConcurrentHashMap<String, Double>();
	public static final Map<String, SMSAlertData> SMS_SEND_LIST = new ConcurrentHashMap<String, SMSAlertData>();
	public static final ConcurrentLinkedQueue<MailData> MAIL_SEND_QUEUE = new ConcurrentLinkedQueue<MailData>();
	
	public static final ConcurrentLinkedQueue<String> KENKUL_RAW_DATA = new ConcurrentLinkedQueue<String>();
	/**水位雨量站資料寫入QUEUQ*/
	public static final ConcurrentLinkedQueue<WaterData> WATER_DATA_INSERT_QUEUE =new ConcurrentLinkedQueue<WaterData>();
	/**水位站資料寫入QUEUE*/
	public static final ConcurrentLinkedQueue<WaterData> RAW_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	/**雨量站資料寫入QUEUE*/
	public static final ConcurrentLinkedQueue<RainData> RAIN_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<RainData>();
}
