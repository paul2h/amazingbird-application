package com.wavegis.global;

import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;

public class GlobalConfig {
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	
	public static int WS_Time_Period = 1000 * 60;
	public static int INSERT_Time_Period = 1000 * 60;
	public static String TrayPassword = "123";
	public static String KillBATPath = "D:/kill.bat";
	public static String WebServiceURL = "http://winfo.tycg.gov.tw/TYSAFE/UPLOADFILE/OPENDATA/WATERLEVEL.XML";

}
