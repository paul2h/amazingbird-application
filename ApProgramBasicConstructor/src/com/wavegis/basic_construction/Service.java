package com.wavegis.basic_construction;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.wavegis.global.tools.LogTool;

public class Service {

	@SuppressWarnings("unused")
	@Autowired
	private Dao dao;

	private Logger logger;

	public Service() {
		initLog();
	}

	private void initLog() {
		logger = LogTool.getLogger(Service.class.getName());
		showMessage("log初始化完成");
	}

	private void showMessage(String message) {
		logger.fatal(message);
	}
}
