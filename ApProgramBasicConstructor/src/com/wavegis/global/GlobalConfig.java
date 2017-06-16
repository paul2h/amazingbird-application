package com.wavegis.global;

import java.util.Properties;

import javax.swing.ImageIcon;

public class GlobalConfig {

	public static final String edition_all = "2017水情介接-完全版8.2";
	public static final String edition_chiayi = "2017水情介接-嘉義縣抽水機1.0版";
	/**
	 * 版本顯示設定
	 */
	public static final String edition = edition_chiayi;

	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");

	public static Properties XML_CONFPIG = new Properties();
}
