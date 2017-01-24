package com.wavegis.global;

import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.ImageIcon;

public class GlobalConfig {
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	
	public static Properties CONFPIG_PROPERTIES = new Properties();
}
