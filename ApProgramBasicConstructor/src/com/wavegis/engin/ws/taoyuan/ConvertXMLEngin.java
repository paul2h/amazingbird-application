package com.wavegis.engin.ws.taoyuan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;

public class ConvertXMLEngin extends TimerEngin {

	private static final String enginID = "ConvertXMLEngin";
	private static final String enginName = "WS轉換資料Engin";
	private static final ConvertXMLEnginView enginView = new ConvertXMLEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public ConvertXMLEngin(){
		setTimeout(GlobalConfig.WS_Time_Period);
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
		showMessage("開始轉換資料...");
		try {
			URL url = new URL(GlobalConfig.WebServiceURL);
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
			List<DynaBean> beanList = new ConvertXMLAnalysisEngin().analysisOriginalData(line);
			
			if(beanList != null){
				for(DynaBean bean : beanList){
					if(bean != null){
						WaterData waterData = new WaterData();
						
						if(bean.get("STATION_ID") != null){
							waterData.setStid(bean.get("STATION_ID").toString());
						}
						if(bean.get("STATION") != null){
							waterData.setStname(bean.get("STATION").toString());
						}
						if(bean.get("DATATIME") != null){
							waterData.setLasttime(new Timestamp(((Date)bean.get("DATATIME")).getTime()));
						}
						if(bean.get("WATERHEIGHT_M") != null){
							waterData.setWaterlevel((double)bean.get("WATERHEIGHT_M"));
						}
						ProxyData.WATER_INSERT_QUEUE.offer(waterData);
					}
				}
				showMessage("資料轉換結束, 共轉換 " + ProxyData.WATER_INSERT_QUEUE.size() + " 筆.");
			}
		} catch(MalformedURLException e){
			e.printStackTrace();
			
			showMessage("資料轉換失敗...");
		} catch(IOException e){
			e.printStackTrace();
			
			showMessage("資料轉換失敗...");
		}
	}

	@Override
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}

}
