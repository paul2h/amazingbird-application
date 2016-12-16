package com.wavegis.global.tools;

import java.io.IOException;
import java.util.Iterator;

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

/**
 * 
 * 針對水利署格式的SOAP進行分析
 * 
 * @author Kiwi
 *
 */
public class WraSoapTool {
	// #[[ SOAP用物件
	static SOAPConnectionFactory soapConnFactory;
	static SOAPConnection SOAPConnection;
	static SOAPMessage SOAPrequest;// 傳出去的request
	static SOAPMessage SOAPrespond;// 接收到的資料respond
	static String respondString;// 從respond轉換過後的String

	// ]] SOAP用物件

	/**
	 * 因不同WS會有不同東西需要微調, 此Demo無法做為通用性範本
	 */
	public static void getWSData(String WebServiceURL) throws IOException {
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
			SOAPrequest.getMimeHeaders().addHeader("SOAPAction", "http://tempuri.org/iCarPos");// 有些WS會需要SOAPAction
			// ]]

			// #[[ 編輯要送出的SOAP訊息
			/* 取得soap物件的body部分 */
			SOAPPart soapPart = SOAPrequest.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			/* 設定namespace */
			envelope.setAttribute("xmlns:tem", "http://tepmuri.org/");
			SOAPBody body = envelope.getBody();
			/* request的參數放入body中 */
			@SuppressWarnings("unused")
			SOAPElement bodyElement = body.addChildElement("tem:iCarPos");
			/* 儲存變更 */
			SOAPrequest.saveChanges();
			// ]]

			// #[[ 送出SOAP訊息 並得到回傳資料
			/* 設定要連到的webservice URL (此URL可從該webservice的wsdl文件中獲得) */
			URLEndpoint urlEndpoint = new URLEndpoint(WebServiceURL);

			/* 發送前顯現發送訊息 */
			System.out.print("發送SOAP:");
			SOAPrequest.writeTo(System.out);
			System.out.println();
			/* 發送訊息 */
			SOAPrespond = SOAPConnection.call(SOAPrequest, urlEndpoint);
			// ]]

			// 顯示資料
			SOAPrespond.writeTo(System.out);

			// 分析資料
			@SuppressWarnings("unchecked")
			Iterator<SOAPElement> iterator = SOAPrespond.getSOAPBody().getChildElements();
			checkData(iterator);// 此分析視回傳的XML而需要做改變
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void checkData(Iterator<SOAPElement> iterator) {
		while (iterator.hasNext()) {
			SOAPElement element = iterator.next();
			if (!element.getTagName().equals("StationRawData") && element.getChildElements().hasNext()) {
				if (element.getValue() == null)
					checkData(element.getChildElements());
			} else if (element.getTagName().equals("PosReport") && element.getChildElements().hasNext()) {
				getData(element.getChildElements());
			}
		}
	}

	private static void getData(Iterator<SOAPElement> iterator) {
		while (iterator.hasNext()) {
			SOAPElement element = iterator.next();
			if (element.getTagName().equals("Dept") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("CarId") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("CarSta") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("Adm") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("time") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("GPS") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("road") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("place") && element.getValue() != null) {
				System.out.println(element.getValue());
			} else if (element.getTagName().equals("lating") && element.getValue() != null) {
				System.out.println(element.getValue());
			}
		}
	}
}
