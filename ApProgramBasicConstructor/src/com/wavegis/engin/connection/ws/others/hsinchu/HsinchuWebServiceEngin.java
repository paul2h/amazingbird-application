package com.wavegis.engin.connection.ws.others.hsinchu;

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

public class HsinchuWebServiceEngin extends TimerEngin{
	private static final String enginID = "HsinchuWebService";
	private static final String enginName = "新竹WebService";
	private static final HsinchuWebServiceEnginView enginView = new HsinchuWebServiceEnginView();
	
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public HsinchuWebServiceEngin(){
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
		showMessage("開始取得資料...");
		
		String[] webServiceURLs = GlobalConfig.XML_CONFIG.getProperty("HsinchuWebServiceURL").split(",");
		
		for(String webServiceURL : webServiceURLs){
			if(!getWaterDataFromWebService(webServiceURL)){
				showMessage("資料取得失敗.\n" + webServiceURL);
				
				break;
			}
		}
		showMessage("資料取得完畢.");
	}

	@Override
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}
	
	private boolean getWaterDataFromWebService(String urlString){
		boolean bSuccess = false;
		try {
			URL url = new URL(urlString);
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
				line = line.substring(greaterIndex + 2).replaceAll(">\\s+<", "><");
			}
			DynaProperty[] dynaPropertys = new DynaProperty[]{
				new DynaProperty("st_no")
				, new DynaProperty("date_time")
				, new DynaProperty("le")
				, new DynaProperty("FORE_H1")
				, new DynaProperty("FORE_H2")
				, new DynaProperty("FORE_H3")
				, new DynaProperty("FORE_H4")
				, new DynaProperty("FORE_H5")
				, new DynaProperty("FORE_H6")
			};
			List<DynaBean> beanList = new ConvertXMLAnalysisEngin("Hsinchu", dynaPropertys).analysisOriginalData(line);
			
			if(beanList != null){
				for(DynaBean bean : beanList){
					if(bean != null){
						WaterData waterData = new WaterData();
						
						if(bean.get("st_no") != null){
							waterData.setStid(bean.get("st_no").toString());
						}
						if(bean.get("date_time") != null){
							Date datetime = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss", Locale.TAIWAN).parse(bean.get("date_time").toString());
							waterData.setLasttime(new Timestamp(datetime.getTime()));
						}
						if(bean.get("le") != null){
							waterData.setWaterlevel(Double.parseDouble(bean.get("le").toString()));
						}
						ProxyData.WATER_INSERT_WATER_QUEUE.offer(waterData);
					}
				}
				bSuccess = true;
			}
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}
		return bSuccess;
	}
}
