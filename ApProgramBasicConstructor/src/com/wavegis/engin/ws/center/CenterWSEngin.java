package com.wavegis.engin.ws.center;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.CenterWebServiceSOAPTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class CenterWSEngin extends TimerEngin {
	private static final String enginID = "CenterWS";
	private static final String enginName = "中央WS接收Engin";
	private static final CenterWSEnginView enginView = new CenterWSEnginView();

	private Logger logger = LogTool.getLogger(CenterWSEngin.class.getName());
	
	public CenterWSEngin() {
		setTimeout(GlobalConfig.WS_Time_Period);
	}

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
	public void timerAction() {
		showMessage("開始取得資料 :");
		try {
			List<WaterData> datas = CenterWebServiceSOAPTool.getWaterLevelData("http://13.76.255.253/WraCenterWebService/ws/WebService");
			for (WaterData data : datas) {
				showMessage(data.getStid() + " " + data.getStname() + " " + data.getLasttime() + " " + data.getWaterlevel());
			}
			ProxyData.WATER_INSERT_QUEUE.addAll(datas);
			showMessage("取得資料完成.");
		} catch (IOException e) {
			showMessage("資料取得錯誤!!");
			e.printStackTrace();
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
