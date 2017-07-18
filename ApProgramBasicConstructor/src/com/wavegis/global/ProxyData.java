package com.wavegis.global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.MailData;
import com.wavegis.model.RainData;
import com.wavegis.model.flood.FloodAlertData;
import com.wavegis.model.water.WaterAlertData;
import com.wavegis.model.water.WaterData;

public class ProxyData {

	public static final Map<String, Double> RIVER_BASINS = new ConcurrentHashMap<String, Double>();
	/** 簡訊警訊發送清單 */
	public static final Map<String, WaterAlertData> SMS_SEND_LIST = new ConcurrentHashMap<String, WaterAlertData>();
	/** 淹水範圍推播清單 */
	public static final Map<String, FloodAlertData> FCM_FLOOD_ALERT_SEND_LIST = new ConcurrentHashMap<>();
	/** 淹水深度推播清單 */
	public static final Map<String, WaterAlertData> FCM_WATERLEVEL_ALERT_SEND_LIST = new ConcurrentHashMap<>();

	public static final ConcurrentLinkedQueue<MailData> MAIL_SEND_QUEUE = new ConcurrentLinkedQueue<MailData>();

	public static final ConcurrentLinkedQueue<String> KENKUL_RAW_DATA = new ConcurrentLinkedQueue<String>();
	/** 水位雨量站資料寫入QUEUQ */
	public static final ConcurrentLinkedQueue<WaterData> WATER_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	/** 水位站資料寫入QUEUE */
	public static final ConcurrentLinkedQueue<WaterData> RAW_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	/** 雨量站資料寫入QUEUE */
	public static final ConcurrentLinkedQueue<RainData> RAIN_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<RainData>();
}
