package com.wavegis.engin.ws.wra;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.tools.CenterWebServiceSOAPTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class WraWSEngin extends TimerEngin {
	private static final String enginID = "WraWS";
	private static final String enginName = "中央WS接收Engin";
	private static final WraWSEnginView enginView = new WraWSEnginView();

	private Logger logger = LogTool.getLogger(WraWSEngin.class.getName());

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
