package com.wavegis.service;

import java.util.Timer;
import java.util.TimerTask;

import com.wavegis.basic.Engin;

/**
 * 
 * 用於定時動作的Engin,已實作Timer部分
 * 
 * @author Kiwi
 *
 */
public abstract class TimerEngin implements Engin {

	private int timerPeriod = 1000;// 預設一秒鐘一次
	private boolean isStarted = false;
	private TimerTask timerTask;

	@Override
	public boolean startEngin() {
		showMessage("準備開啟Engin...");
		timerTask = new TimerTask() {
			@Override
			public void run() {
				timerAction();
			}
		};
		new Timer().schedule(timerTask, 1000, timerPeriod);
		isStarted = true;
		showMessage("Engin已啟動");
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("準備停止Engin");
		boolean stopSuccess = timerTask.cancel();
		if (stopSuccess) {
			isStarted = false;
			timerTask = null;
			showMessage("Engin已停止");
		} else {
			showMessage("停止失敗");
		}
		return stopSuccess;
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	/**
	 * 設定timer的時間間距
	 * 
	 * @param millesecond
	 */
	protected void setTimeout(int millesecond) {
		this.timerPeriod = millesecond;
	}

	/**
	 * timer要做的東西
	 */
	public abstract void timerAction();

	public abstract void showMessage(String message);
}
