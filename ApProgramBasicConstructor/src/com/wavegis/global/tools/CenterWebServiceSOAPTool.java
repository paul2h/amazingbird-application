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

import com.wavegis.model.water.WaterData;

/**
 * 
 * 中央格式WebService資料接收工具
 * 
 * @version 1.0
 * @author Kiwi
 *
 */
public class CenterWebServiceSOAPTool {

	// #[[ SOAP用物件
	static SOAPConnectionFactory soapConnFactory;
	static SOAPConnection SOAPConnection;
	static SOAPMessage SOAPrequest;// 傳出去的request
	static SOAPMessage SOAPrespond;// 接收到的資料respond
	static String respondString;// 從respond轉換過後的String

	// ]] SOAP用物件

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	

	// #[[ 水位function
	/**
	 * function : getRawDatas
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
			envelope.setAttribute("xmlns:ws", "ws");
			SOAPBody body = envelope.getBody();
			/* request的參數放入body中 */
			@SuppressWarnings("unused")
			SOAPElement bodyElement = body.addChildElement("ws:getRawDatas");
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
//			 SOAPrespond.writeTo(System.out);

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
			
			if (!element.getTagName().equals("datas") && element.getChildElements().hasNext()) {
				if (element.getValue() == null) {
					return checkWaterData(element.getChildElements());
				}
			} else if (element.getTagName().equals("datas") && element.getChildElements().hasNext()) {
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
				case "name_c":
					waterData.setStname(dataElement.getValue());
					break;
				case "stage":
					waterData.setWaterlevel(Double.valueOf(dataElement.getValue()));
					break;
				case "st_no":
					waterData.setStid(dataElement.getValue());
					break;
				case "RECDATE":
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

}
