package com.wavegis.engin.connection.ws.others.hsinchu.city;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.WGWebServiceSOAPTool;
import com.wavegis.model.RainData;

public class HsinchuCityWebServiceEngin extends TimerEngin {

	public static final String enginID = "HsinchuCityWebServiceEngin";
	private static final String enginName = "新竹市WebServiceEngin";
	private static final String HsinshuAppServiceURL = GlobalConfig.XML_CONFIG.getProperty("HsinchuCityWebServiceURL");
	private static final HsinchuCityWebServiceEnginView enginView = new HsinchuCityWebServiceEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	public HsinchuCityWebServiceEngin(){
		setTimeout(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_WS"));
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
	public void timerAction(){
		try {
			showMessage("開始取得新竹市雨量資料...");
			List<RainData> rainDatas = WGWebServiceSOAPTool.getRainData(HsinshuAppServiceURL);
			for(RainData rainData : rainDatas){
				showMessage(rainData.getStid() + " " + rainData.getStname() + "  " + rainData.getTime() + " " + rainData.getHour_1());
				ProxyData.RAIN_DATA_INSERT_QUEUE.offer(rainData);
			}
			showMessage("新竹市雨量資料取得完畢.");
		} catch (IOException e) {
			showMessage("新竹市雨量資料取得錯誤 : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
