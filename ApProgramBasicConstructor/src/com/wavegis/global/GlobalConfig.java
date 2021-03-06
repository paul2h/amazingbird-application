package com.wavegis.global;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import com.wavegis.model.CCTVData;
import com.wavegis.model.WebMonitorFocusData;
import com.wavegis.model.water.WaterData;
@SuppressWarnings("unused")
public class GlobalConfig {

	private static final String edition_number_string = "1.0";

	private static final String edition_all = "完全版";
	private static final String edition_changhua = "彰化";
	private static final String edition_chiayi = "嘉義縣抽水機";
	private static final String edition_chiayi_city = "嘉義市水情";
	private static final String edition_keelung = "基隆水情";
	private static final String edition_lockist = "Lockist版";
	private static final String edition_web_monitor = "網頁監控";
	private static final String edition_wra02 = "二河局";
	private static final String edition_yilan = "宜蘭";
	
	/**
	 * 版本顯示設定
	 */
	public static final String edition = String.format("2017水情總介接%s版-%s" ,edition_number_string ,edition_lockist);//FIXME 更換縣市需設定
	
	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	
	private static final String FrameIconImagePath_Changhua = "./conf/icon/icon_changhua.png";
	private static final String FrameIconImagePath_ChiayiCity = "./conf/icon/icon_chiayi_city.png";
	public static final String FrameIconImagePath_ChiayiCounty = "./conf/icon/icon_chiayi_county.png";
	private static final String FrameIconImagePath_Keelung = "./conf/icon/icon_keelung.png";
	private static final String FrameIconImagePath_WebMonitor = "./conf/icon/icon_web_monitor.png";
	private static final String FrameIconImagePath_Wra02 = "./conf/icon/icon_wra02.png";
	private static final String FrameIconImagePath_Yilan = "./conf/icon/icon_yilan.png";
	
	/** 客製化車機訊息判斷key(為與舊版AVLS相容) */
	public static String CustomMessageKey = "from_old";
	
	/**
	 * 介接icon設定
	 */
	public static final ImageIcon FrameIconImage = new ImageIcon(FrameIconImagePath_Changhua);//FIXME 更換縣市需設定

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static Properties XML_CONFIG = new Properties();
}
