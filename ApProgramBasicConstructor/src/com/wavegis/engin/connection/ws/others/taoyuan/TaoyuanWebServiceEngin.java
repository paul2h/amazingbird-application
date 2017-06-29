package com.wavegis.engin.connection.ws.others.taoyuan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.logging.log4j.Logger;

import com.wavegis.engin.connection.ws.others.ConvertXMLAnalysisEngin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.WaterData;

public class TaoyuanWebServiceEngin extends TimerEngin {

	private static final String enginID = "TaoyuanWebService";
	private static final String enginName = "桃園WebService";
	private static final TaoyuanWebServiceEnginView enginView = new TaoyuanWebServiceEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public TaoyuanWebServiceEngin(){
		setTimeout(GlobalConfig.XML_CONFIG.getProperty("WS_Time_Period"));
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
			URL url = new URL(GlobalConfig.XML_CONFIG.getProperty("TaoyuanWebServiceURL"));
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
			DynaProperty[] dynaPropertys = new DynaProperty[]{
				new DynaProperty("DATATIME")
				, new DynaProperty("LON")
				, new DynaProperty("LAT")
				, new DynaProperty("STATION")
				, new DynaProperty("STATION_ID")
				, new DynaProperty("TOWN")
				, new DynaProperty("REVETMENT_M")
				, new DynaProperty("WATERHEIGHT_M")
				, new DynaProperty("PHOTO_PATH")
				, new DynaProperty("STREAM_HTML")
				, new DynaProperty("STATUS")
			};
			List<DynaBean> beanList = new ConvertXMLAnalysisEngin("WaterLevel", dynaPropertys).analysisOriginalData(line);
			
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
							Date datetime = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss", Locale.TAIWAN).parse(bean.get("DATATIME").toString());
							waterData.setLasttime(new Timestamp(datetime.getTime()));
						}
						if(bean.get("WATERHEIGHT_M") != null){
							waterData.setWaterlevel(Double.parseDouble(bean.get("WATERHEIGHT_M").toString()));
						}
						ProxyData.WATER_INSERT_WATER_QUEUE.offer(waterData);
					}
				}
				showMessage("資料轉換結束, 共轉換 " + ProxyData.WATER_INSERT_WATER_QUEUE.size() + " 筆.");
			}
		} catch(MalformedURLException e){
			e.printStackTrace();
			
			showMessage("資料轉換失敗...");
		} catch(IOException e){
			e.printStackTrace();
			
			showMessage("資料轉換失敗...");
		} catch(ParseException e){
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
