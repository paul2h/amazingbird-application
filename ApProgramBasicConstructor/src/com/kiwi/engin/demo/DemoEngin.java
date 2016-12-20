package com.kiwi.engin.demo;

import org.apache.logging.log4j.Logger;

import com.kiwi.engin.Engin;
import com.kiwi.engin.EnginView;
import com.kiwi.global.tools.LogTool;

public class DemoEngin implements Engin {

	private static final String enginID = "demo";
	private static final String engninName = "測試用Engin";
	private static final DemoEnginView demoEnginView = new DemoEnginView();
	private boolean running = false;
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return engninName;
	}

	@Override
	public boolean startEngin() {
		showMessage("demo engin start");
		running = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("demo engin stop");
		running = false;
		return true;
	}

	private void showMessage(String message) {
		demoEnginView.showMessage(message);
		logger.info(message);
	}

	@Override
	public boolean isStarted() {
		return running;
	}

	@Override
	public EnginView getEnginView() {
		return demoEnginView;
	}

}
