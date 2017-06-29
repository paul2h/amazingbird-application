package com.wavegis.engin.connection.ws.soap.wavegis;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.WGWebServiceSOAPTool;
import com.wavegis.model.RainData;
import com.wavegis.model.water.WaterData;

public class WavegisWSEngin extends TimerEngin {

	public static final String enginID = "WavegisWS";
	private static final String enginName = "昕傳WS讀取Engin";
	private static final WavegisWSEnginView enginView = new WavegisWSEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

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
		try {
			showMessage("水位:");
			List<WaterData> datas = WGWebServiceSOAPTool.getWaterLevelData("http://210.69.166.179/ChiayiCounty/ws/WaterLevelAppService");
			for (WaterData waterData : datas) {
				
				showMessage(waterData.getStid()+" "+waterData.getWaterlevel()+" "+waterData.getLasttime()+" "+waterData.getVoltage());
			}
			showMessage("雨量:");
			List<RainData> rainDatas = WGWebServiceSOAPTool.getRainData("http://210.69.166.179/ChiayiCounty/ws/WaterLevelAppService");
			for (RainData rainData : rainDatas) {
				showMessage(rainData.getStid()+" "+rainData.getMin_10()+" "+rainData.getLasttime()+" "+rainData.getRain_current());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
