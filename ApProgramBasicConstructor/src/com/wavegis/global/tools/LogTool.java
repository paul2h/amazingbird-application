package com.wavegis.global.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;

import com.wavegis.global.GlobalConfig;

public class LogTool {

	public static LoggerContext context;

	/**
	 * 取得log編寫物件
	 * 
	 * @param className
	 *            class完整名稱 "com.wavegis.XXXX"
	 * @return
	 */
	public static Logger getLogger(String className) {
		if (context == null) {
			initLogger();
		}
		return context.getLogger(className);
	}

	private static void initLogger() {
		try {
			ConfigurationFactory factory = XmlConfigurationFactory.getInstance();
			ConfigurationSource configurationSource = new ConfigurationSource(new FileInputStream(new File(GlobalConfig.LogSettingPath)));
			Configuration configuration = factory.getConfiguration(configurationSource);
			context = new LoggerContext("JournalDevLoggerContext");
			context.start(configuration);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
