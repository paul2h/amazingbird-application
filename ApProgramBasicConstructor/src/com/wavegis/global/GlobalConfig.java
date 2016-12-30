package com.wavegis.global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.wavegis.model.CCTVData;

public class GlobalConfig {

	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static int TimerPeriod = 1000;
	public static int TimerPeriod_DB = 3600000;
	public static int TimerPeriod_SMS = 1000;
	public static String TrayPassword = "123";
	public static String KillBATPath = "D:/kill.bat";
	// CCTV
	public static List<CCTVData> CCTV_DATA_LIST = new ArrayList<CCTVData>();
	public static String CCTVImagePath = "D:/";
	// 圖片
	public static String ImageDirPath = "D:/";
	public static String ImageNewDirPath = "D:/";
	// 簡訊發送
	public static String SMS_Account = "11562";
	public static String SMS_Password = "11562";

}
