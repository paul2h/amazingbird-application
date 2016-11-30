package com.kiwi.conf;

import javax.swing.ImageIcon;

import com.kiwi.service.Engin;
import com.kiwi.service.demo.DemoEngin;
import com.kiwi.ui.EnginView;
import com.kiwi.ui.demo.DemoEnginView;

public class GlobalConfig {
	
	public static final String Spring_conf_path = "./spring-config.xml";
	public static final String Conf_XML_path = "./conf/conf.xml";
	public static final String MyBatisConfig_XML_Path = "./conf/myBatisConfig.xml";
	public static final ImageIcon FrameIconImage = new ImageIcon("./conf/icon.png");
	public static final Engin[] Engins = {
		new DemoEngin()
	};
	
	public static final EnginView[] EnginViews = {
		new DemoEnginView()
	};
	
	public static String TrayPassword ="123";
	public static String KillBATPath = "D:/kill.bat";

}
