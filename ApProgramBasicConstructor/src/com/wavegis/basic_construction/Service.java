package com.wavegis.basic_construction;

import org.apache.logging.log4j.Logger;

import com.wavegis.global.tools.LogTool;

public class Service {

//	@Autowired
//	private Dao dao;目前基本架構DAO無用到

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
