package com.kiwi.service.demo;

import com.kiwi.service.Engin;
import com.kiwi.ui.demo.DemoEnginView;

public class DemoEngin implements Engin {

	private static final String enginID = "demo";
	private static final String engninName = "測試用Engin";
	private DemoEnginView demoEnginView;
	private boolean running = false;

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
		if (demoEnginView == null) {
			demoEnginView = DemoEnginView.getInstance();
		}
		demoEnginView.showMessage(message);
	}

	@Override
	public boolean isStarted() {
		return running;
	}

}
