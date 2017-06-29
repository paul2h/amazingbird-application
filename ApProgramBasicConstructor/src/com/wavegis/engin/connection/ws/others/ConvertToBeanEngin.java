package com.wavegis.engin.connection.ws.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class ConvertToBeanEngin extends TimerEngin {

	public static final String enginID = "ConvertToBeanEngin";
	private static final String enginName = "WS轉換成BeanEngin";
	private static final ConvertToBeanEnginView enginView = new ConvertToBeanEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public ConvertToBeanEngin(){
		setTimeout(60000);
	}
	
	@Override
	public String getEnginID(){
		return enginID;
	}

	@Override
	public String getEnginName(){
		return enginName;
	}

	@Override
	public EnginView getEnginView(){
		return enginView;
	}

	@Override
	public void timerAction(){
		try {
			URL url = new URL(GlobalConfig.XML_CONFIG.getProperty("WebServiceURL"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();

			line = sb.toString().trim();
			
			int greaterIndex = line.indexOf("?>");
			
			if(greaterIndex > 0){
				line = line.substring(greaterIndex + 2);
			}
			List<DynaBean> beanList = new ConvertToBeanAnalysisEngin().analysisOriginalData(line);
			
			showMessage("beanListSize " + String.valueOf(beanList.size()));
			
			for(DynaBean bean : beanList){
				if(bean.get("DATATIME") != null){
					showMessage("STATION_ID " + bean.get("STATION_ID") + " " + bean.get("STATION"));
					showMessage("TOWN " + bean.get("TOWN") + " " + bean.get("LON").toString() + " " + bean.get("LAT").toString());
					showMessage("DATATIME " + bean.get("DATATIME").toString());
					showMessage("REVETMENT " + bean.get("REVETMENT_M") + "M");
					showMessage("WATERHEIGHT " + bean.get("WATERHEIGHT_M") + "M");
					showMessage("PHOTO_PATH " + bean.get("PHOTO_PATH"));
					showMessage("STREAM_HTML " + bean.get("STREAM_HTML"));
					showMessage("STATUS " + bean.get("STATUS"));
					showMessage("");
				}
			}
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		/*
		try {
			showMessage("水位:");
			List<WaterData> datas = WGWebServiceSOAPTool.getWaterLevelData("http://210.69.166.179/ChiayiCounty/ws/WaterLevelAppService");
			for (WaterData waterData : datas) {
				
				showMessage(waterData.getStid()+" "+waterData.getWaterlevel()+" "+waterData.getLasttime()+" "+waterData.getVoltage());
			}
			showMessage("雨量:");
			List<RainData> rainDatas = WGWebServiceSOAPTool.getRainData("http://210.69.166.179/ChiayiCounty/ws/WaterLevelAppService");
			for (RainData rainData : rainDatas) {
				showMessage(rainData.getStno()+" "+rainData.getMin_10()+" "+rainData.getLasttime()+" "+rainData.getRain());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}

}
