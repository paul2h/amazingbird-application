package com.wavegis.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.CCTVData;
import com.wavegis.model.MailData;
import com.wavegis.model.RainData;
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.flood.FloodAlertData;
import com.wavegis.model.water.WaterAlertData;
import com.wavegis.model.water.WaterData;

public class ProxyData {
	/** 各水位站底層高 */
	public static final ConcurrentHashMap<String, WaterData> RIVER_BOTTOM_DATAS = new ConcurrentHashMap<>();
	/** CCTV清單 */
	public static final List<CCTVData> CCTV_DATA_LIST = new ArrayList<CCTVData>();
	/** 監控Engine監測連結清單 */
	public static final List<WebMonitorFocusData> WEB_MONITOR_URL_LIST = new ArrayList<WebMonitorFocusData>();
	/** 塏固Gateway對照Map */
	public static final Map<String, String> KENKUL_GATEWAY_ID_MAP = new ConcurrentHashMap<>();
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
