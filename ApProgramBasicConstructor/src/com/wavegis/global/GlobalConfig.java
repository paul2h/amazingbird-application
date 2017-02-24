package com.wavegis.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.wavegis.model.CCTVData;

public class GlobalConfig {

	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");

	public static Properties XML_CONFIG = new Properties();
	public static List<CCTVData> CCTV_DATA_LIST = new ArrayList<CCTVData>();

}
