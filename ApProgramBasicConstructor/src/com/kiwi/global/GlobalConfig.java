package com.kiwi.global;

import java.util.HashMap;

import javax.swing.ImageIcon;

import com.kiwi.basic.Engin;
import com.kiwi.basic.EnginView;
import com.kiwi.service.demo.DemoEngin;

public class GlobalConfig {

	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./myBatisConfig.xml";
	public static final String MyBatisConfig_XML_Path_Output = "./conf/myBatisConfig.xml";// 匯出後jar檔要執行的路徑
	public static final String LogSettingPath = "./conf/log4j2.xml";
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");

	/** 掛入的Engin清單(增減Engin改這個就好 View會自己變) */
	public static final Engin[] Engins = { new DemoEngin() };

	@SuppressWarnings("serial")
	public static final HashMap<String, EnginView> EnginViewMap = new HashMap<String, EnginView>() {
		{
			for (Engin engin : Engins) {
				put(engin.getEnginID(), engin.getEnginView());
			}
		}
	};

	public static final EnginView[] EnginViews = (EnginView[]) EnginViewMap.values()
			.toArray(new EnginView[Engins.length]);

	public static String TrayPassword = "123";
	public static String KillBATPath = "D:/kill.bat";

}
