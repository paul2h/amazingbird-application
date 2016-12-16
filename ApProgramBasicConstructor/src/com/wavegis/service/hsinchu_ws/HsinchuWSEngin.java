package com.wavegis.service.hsinchu_ws;

import com.wavegis.basic.EnginView;
import com.wavegis.service.TimerEngin;
import com.wavegis.ui.hsinchu_ws.HsinchuWSEnginView;

public class HsinchuWSEngin extends TimerEngin {

	private static final String enginID = "HsinchuWS";
	private static final String enginName = "新竹縣市WS接收Engin";
	private static final HsinchuWSEnginView enginView = new HsinchuWSEnginView();

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}
	
	@Override
	public boolean startEngin() {
		setTimeout(5000);
		return super.startEngin();
	}

	@Override
	public void timerAction() {
		getHsinchuCountyData();
		getHsinchuCityData();
	}
	
	private void getHsinchuCountyData(){
		showMessage("取得新竹縣水位資料...");
		showMessage("取得新竹縣雨量資料...");
	}
	
	private void getHsinchuCityData(){
		showMessage("取得新竹市水位資料...");
		showMessage("取得新竹市雨量資料...");		
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
	}

}
