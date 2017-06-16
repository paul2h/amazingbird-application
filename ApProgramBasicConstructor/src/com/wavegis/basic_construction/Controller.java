package com.wavegis.basic_construction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.global.EnginCenter;

public class Controller {

	private List<Engin> engins = EnginCenter.Engins;

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
