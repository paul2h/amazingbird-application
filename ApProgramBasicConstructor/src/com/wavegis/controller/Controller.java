package com.wavegis.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.wavegis.basic.Engin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.service.Service;
import com.wavegis.ui.MainUI;

public class Controller {

	Engin[] engins = GlobalConfig.Engins;

	@Autowired
	MainUI mainUI;

	@Autowired
	Service service;

	public void startApplication(String edition) {
		mainUI.start(edition);
	}

	public void startEngin(String enginID) {
		for (Engin engin : engins) {
			if (engin.getEnginID().equals(enginID)) {
				engin.startEngin();
			}
		}
	}

	public void stopEngin(String enginID) {
		for (Engin engin : engins) {
			if (engin.getEnginID().equals(enginID)) {
				engin.stopEngin();
			}
		}
	}

	/** 取得指定Engin狀態 */
	public boolean isEnginStarted(String enginID) {
		for (Engin engin : engins) {
			if (engin.getEnginID().equals(enginID)) {
				return engin.isStarted();
			}
		}
		return false;
	}

}