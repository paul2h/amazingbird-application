package com.wavegis.global.tools;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.messaging.URLEndpoint;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.wavegis.model.RainData;
import com.wavegis.model.water.WaterData;

/**
 * 
 * 昕傳WebService資料接收工具
 * 
 * @version 2.0
 * @author Kiwi
 *
 */
public class WGWebServiceSOAPTool {

	// #[[ SOAP用物件
	static SOAPConnectionFactory soapConnFactory;
	static SOAPConnection SOAPConnection;
	static SOAPMessage SOAPrequest;// 傳出去的request
	static SOAPMessage SOAPrespond;// 接收到的資料respond
	static String respondString;// 從respond轉換過後的String

	// ]] SOAP用物件

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	// #[[ 水位function
	/**
	 * function : getWaterLevel
	 * 
	 * @return
	 */
	public static List<WaterData> getWaterLevelData(String WebServiceURL) throws IOException {
		/* 開始從WS取資料 */
		try {
			// #[[ 第一次創建連接
			if (soapConnFactory == null) {
				soapConnFactory = SOAPConnectionFactory.newInstance();
				SOAPConnection = soapConnFactory.createConnection();
			}
			// ]]

			// #[[ 創建要傳出的SOAP訊息
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPrequest = messageFactory.createMessage();
			SOAPrequest.getMimeHeaders().addHeader("SOAPAction", "");// 有些WS會需要SOAPAction
			// ]]

			// #[[ 編輯要送出的SOAP訊息
			/* 取得soap物件的body部分 */
			SOAPPart soapPart = SOAPrequest.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			/* 設定namespace */
			envelope.setAttribute("xmlns:tep", "http://tepmuri.org/");
			SOAPBody body = envelope.getBody();
			/* request的參數放入body中 */
			@SuppressWarnings("unused")
			SOAPElement bodyElement = body.addChildElement("tep:getWaterLevel");
			/* 儲存變更 */
			SOAPrequest.saveChanges();
			// ]]

			// #[[ 送出SOAP訊息 並得到回傳資料
			/* 設定要連到的webservice URL (此URL可從該webservice的wsdl文件中獲得) */
			URLEndpoint urlEndpoint = new URLEndpoint(WebServiceURL);

			/* 發送前顯現發送訊息 */
			// System.out.print("發送SOAP:");
			// SOAPrequest.writeTo(System.out);
			// System.out.println();
			/* 發送訊息 */
			SOAPrespond = SOAPConnection.call(SOAPrequest, urlEndpoint);
			// ]]

			// 顯示資料
			// SOAPrespond.writeTo(System.out);

			// 分析資料
			@SuppressWarnings("unchecked")
			Iterator<SOAPElement> iterator = SOAPrespond.getSOAPBody().getChildElements();
			return checkWaterData(iterator);// 此分析視回傳的XML而需要做改變
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<WaterData> checkWaterData(Iterator<SOAPElement> iterator) {
		while (iterator.hasNext()) {
			SOAPElement element = (SOAPElement) iterator.next();
			if (element.getTagName().equals("good")) {// 查詢成功與否
				if (!Boolean.valueOf(element.getValue())) {
					System.out.println("資料查詢錯誤");
					return null;
				}
			}
			if (!element.getTagName().equals("result") && element.getChildElements().hasNext()) {
				if (element.getValue() == null) {
					return checkWaterData(element.getChildElements());
				}
			} else if (element.getTagName().equals("result") && element.getChildElements().hasNext()) {
				return getWaterData(element.getChildElements());
			}
		}
		return null;
	}

	private static List<WaterData> getWaterData(Iterator<SOAPElement> iterator) {
		List<WaterData> waterDatas = new ArrayList<>();
		while (iterator.hasNext()) {
			boolean hasError = false;
			WaterData waterData = new WaterData();
			SOAPElement element = (SOAPElement) iterator.next();
			@SuppressWarnings("unchecked")
			Iterator<SOAPElement> dataIterator = element.getChildElements();
			while (dataIterator.hasNext() && !hasError) {

				SOAPElement dataElement = (SOAPElement) dataIterator.next();
				String tagName = dataElement.getTagName();
				switch (tagName) {
				case "battery":
					waterData.setVoltage(Double.valueOf(dataElement.getValue()));
					break;
				case "data":
					waterData.setWaterlevel(Double.valueOf(dataElement.getValue()));
					break;
				case "stno":
					waterData.setStid(dataElement.getValue());
					break;
				case "time":
					try {
						waterData.setLasttime(new Timestamp(sdf.parse(dataElement.getValue()).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
						hasError = true;
					}
					break;

				default:
					System.out.println("未知的tag : " + tagName);
					break;
				}
			}
			if (!hasError) {
				waterDatas.add(waterData);
			}
		}

		return waterDatas;
	}
	// ]]

	// #[[ 雨量function
	/**
	 * function : getRainBase
	 * 
	 * @return
	 */
	public static List<RainData> getRainData(String WebServiceURL) throws IOException {
		/* 開始從WS取資料 */
		try {
			// #[[ 第一次創建連接
			if (soapConnFactory == null) {
				soapConnFactory = SOAPConnectionFactory.newInstance();
				SOAPConnection = soapConnFactory.createConnection();
			}
			// ]]

			// #[[ 創建要傳出的SOAP訊息
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPrequest = messageFactory.createMessage();
			SOAPrequest.getMimeHeaders().addHeader("SOAPAction", "");// 有些WS會需要SOAPAction
			// ]]

			// #[[ 編輯要送出的SOAP訊息
			/* 取得soap物件的body部分 */
			SOAPPart soapPart = SOAPrequest.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			/* 設定namespace */
			envelope.setAttribute("xmlns:tep", "http://tepmuri.org/");
			SOAPBody body = envelope.getBody();
			/* request的參數放入body中 */
			@SuppressWarnings("unused")
			SOAPElement bodyElement = body.addChildElement("tep:getRainBase");
			/* 儲存變更 */
			SOAPrequest.saveChanges();
			// ]]

			// #[[ 送出SOAP訊息 並得到回傳資料
			/* 設定要連到的webservice URL (此URL可從該webservice的wsdl文件中獲得) */
			URLEndpoint urlEndpoint = new URLEndpoint(WebServiceURL);

			/* 發送前顯現發送訊息 */
			// System.out.print("發送SOAP:");
			// SOAPrequest.writeTo(System.out);
			// System.out.println();
			/* 發送訊息 */
			SOAPrespond = SOAPConnection.call(SOAPrequest, urlEndpoint);
			// ]]

			// 顯示資料
			// SOAPrespond.writeTo(System.out);

			// 分析資料
			@SuppressWarnings("unchecked")
			Iterator<SOAPElement> iterator = SOAPrespond.getSOAPBody().getChildElements();
			return checkRainData(iterator);// 此分析視回傳的XML而需要做改變
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<RainData> checkRainData(Iterator<SOAPElement> iterator) {
		while (iterator.hasNext()) {
			SOAPElement element = (SOAPElement) iterator.next();
			if (element.getTagName().equals("good")) {// 查詢成功與否
				if (!Boolean.valueOf(element.getValue())) {
					System.out.println("資料查詢錯誤");
					return null;
				}
			}
			if (!element.getTagName().equals("result") && element.getChildElements().hasNext()) {
				if (element.getValue() == null) {
					return checkRainData(element.getChildElements());
				}
			} else if (element.getTagName().equals("result") && element.getChildElements().hasNext()) {
				return getRainData(element.getChildElements());
			}
		}
		return null;
	}

	private static List<RainData> getRainData(Iterator<SOAPElement> iterator) {
		List<RainData> rainDatas = new ArrayList<>();
		while (iterator.hasNext()) {
			boolean hasError = false;
			RainData rainData = new RainData();
			SOAPElement element = (SOAPElement) iterator.next();
			@SuppressWarnings("unchecked")
			Iterator<SOAPElement> dataIterator = element.getChildElements();
			while (dataIterator.hasNext() && !hasError) {

				SOAPElement dataElement = (SOAPElement) dataIterator.next();
				String tagName = dataElement.getTagName();
				switch (tagName) {
				case "stno":
					rainData.setStid(dataElement.getValue());
					break;
				case "stnm":
					rainData.setStname(dataElement.getValue());
					break;
				case "time":
					try {
						rainData.setLasttime(Timestamp.valueOf(dataElement.getValue()));
					} catch (Exception e) {
						e.printStackTrace();
						hasError = true;
					}
					break;
				case "min_10":
					rainData.setMin_10(Double.valueOf(dataElement.getValue()));
					break;
				case "rain":
					rainData.setRain_current(Double.valueOf(dataElement.getValue()));
					break;
				case "hour_3":
					rainData.setHour_3(Double.valueOf(dataElement.getValue()));
					break;
				case "hour_6":
					rainData.setHour_6(Double.valueOf(dataElement.getValue()));
					break;
				case "hour_12":
					rainData.setHour_12(Double.valueOf(dataElement.getValue()));
					break;
				case "hour_24":
					rainData.setHour_24(Double.valueOf(dataElement.getValue()));
					break;
				default:
					System.out.println("未知的tag : " + tagName);
					break;
				}
			}
			if (!hasError) {
				rainDatas.add(rainData);
			}
		}

		return rainDatas;
	}
	// ]]
}
