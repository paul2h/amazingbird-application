package com.wavegis.engin.connection.ws.others.hsinchu.city;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class HsinchuCityWebServiceEngin extends TimerEngin {

	private static final String enginID = "HsinchuCityWebServiceEngin";
	private static final String enginName = "新竹市WebServiceEngin";
	private static final HsinchuCityWebServiceEnginView enginView = new HsinchuCityWebServiceEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	public HsinchuCityWebServiceEngin(){
		setTimeout(GlobalConfig.CONFPIG_PROPERTIES.getProperty("WS_Time_Period"));
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
		showMessage("開始取得資料...");
		
		try {
			SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection SOAPConnection = soapConnFactory.createConnection();
			SOAPMessage SOAPrequest = MessageFactory.newInstance().createMessage();
			SOAPPart soapPart = SOAPrequest.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			
			envelope.setAttribute("xmlns:tep", "http://tepmuri.org");
			envelope.getBody().addChildElement("tep:getRainBase");
			
			SOAPrequest.saveChanges();
			
			//SOAPMessage SOAPrespond = SOAPConnection.call(SOAPrequest, new URLEndpoint(GlobalConfig.HsinchuCityWebServiceURL));
			SOAPMessage SOAPrespond = SOAPConnection.call(SOAPrequest, new URL(new URL("http://210.241.16.73:8080/"), "/Hsinchu/ws/WaterLevelAppService", new URLStreamHandler(){
				
				@Override
				protected URLConnection openConnection(URL url) throws IOException{
					URLConnection connection = new URL(url.toString()).openConnection();
					
					connection.setConnectTimeout(1000 * 60 * 10);
					connection.setReadTimeout(1000 * 60);
					
					return (connection);
				}
			}));
			
			System.out.println(SOAPrespond.toString());
			/*
			Iterator<SOAPElement> iterator = SOAPrespond.getSOAPBody().getChildElements();
			
			while (iterator.hasNext()){
				SOAPElement element = (SOAPElement)iterator.next();
				String elementTagName = element.getTagName();.
				
				if(element.getChildElements().hasNext()){
					
				}
				
				if (!"RainBase".equals(elementTagName) && element.getChildElements().hasNext()) {
					if (element.getValue() == null)
						checkTag_rain(element.getChildElements());
				} else if (element.getTagName().equals("RainBase") && element.getChildElements().hasNext()) {
					getData_rain(element.getChildElements());
				}
			}
			*/
		} catch(UnsupportedOperationException e){
			e.printStackTrace();
		} catch(SOAPException e){
			e.printStackTrace();
		} catch(MalformedURLException e){
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
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
