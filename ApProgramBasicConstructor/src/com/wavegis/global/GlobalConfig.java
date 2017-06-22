package com.wavegis.global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.ImageIcon;
import com.wavegis.model.CCTVData;
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.WaterData;

public class GlobalConfig {

	public static final String edition_all = "2017水情介接-完全版8.2";
	public static final String edition_chiayi = "2017水情介接-嘉義縣抽水機2.0版";
	public static final String edition_chiayi_city = "2017水情介接-嘉義市水情6.1版";
	public static final String edition_keelung = "2017水情介接-基隆水情1.0版";
	public static final String edition_web_monitor = "2017水情介接-網頁監控1.0版";
	public static final String edition_wra02 = "2017水情介接-二河局2.6版";
	
	/**
	 * 版本顯示設定
	 */
	public static final String edition = edition_wra02;
	
	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	
	public static final String FrameIconImagePath_ChiayiCity = "./conf/icon/icon_chiayi_city.png";
	public static final String FrameIconImagePath_Keelung = "./conf/icon/icon_keelung.png";
	public static final String FrameIconImagePath_WebMonitor = "./conf/icon/icon_web_monitor.png";
	public static final String FrameIconImagePath_Wra02 = "./conf/icon/icon_wra02.png";
	
	/**
	 * 介接icon設定
	 */
	public static final ImageIcon FrameIconImage = new ImageIcon(FrameIconImagePath_Wra02);

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static List<CCTVData> CCTV_DATA_LIST = new ArrayList<CCTVData>();
	public static ConcurrentHashMap<String, WaterData> RIVER_BOTTOM_DATAS = new ConcurrentHashMap<>();
	public static List<WebMonitorFocusData> WEB_MONITOR_URL_LIST = new ArrayList<WebMonitorFocusData>();

	public static Properties XML_CONFIG = new Properties();
}
