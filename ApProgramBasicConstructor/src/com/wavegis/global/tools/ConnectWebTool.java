package com.wavegis.global.tools;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;

public class ConnectWebTool {

	/** 測試網頁連結正常 */
	public static String tryConnect(String urlString) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlString);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("User-agent", "IE/6.0");
			uc.setReadTimeout(10000);// 設定timeout時間 - 10秒
			uc.connect();// 連線
			sb.append(urlString+"\n");
			int status = uc.getResponseCode();
			sb.append("回傳代碼 : " + status + "\n");
			switch (status) {
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:// 504
				sb.append("連線網址逾時!\n");
				break;
			case HttpURLConnection.HTTP_FORBIDDEN:// 403
				sb.append("連線網址禁止!\n");
				break;
			case HttpURLConnection.HTTP_INTERNAL_ERROR:// 500
				sb.append("連線網址錯誤或不存在!\n");
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:// 404
				sb.append("連線網址不存在!\n");
				break;
			case HttpURLConnection.HTTP_OK:
				sb.append("OK!\n");
				break;
			}
		} catch (java.net.MalformedURLException e) {
			sb.append("網址格式錯誤!!!\n");
			e.printStackTrace();
		} catch (java.io.IOException e) {
			sb.append("連線有問題!!!!!!\n");
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/** 測試網頁連結正常 */
	public static synchronized boolean tryURLExist(String urlString) {
		boolean result = false;
		try {
			URL url = new URL(urlString);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("User-agent", "IE/6.0");
			uc.setReadTimeout(30000);// 設定timeout時間
			uc.connect();// 連線
			System.out.println(urlString);
			System.out.println("網址/ip位置: " + Inet4Address.getByName(url.getHost()));
			int status = uc.getResponseCode();
			System.out.println("HTTP response code: " + status);
			switch (status) {
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:// 504
				System.out.println("連線網址逾時!");
				break;
			case HttpURLConnection.HTTP_FORBIDDEN:// 403
				System.out.println("連線網址禁止!");
				break;
			case HttpURLConnection.HTTP_INTERNAL_ERROR:// 500
				System.out.println("連線網址錯誤或不存在!");
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:// 404
				System.out.println("連線網址不存在!");
				break;
			case HttpURLConnection.HTTP_OK:
				System.out.println("OK!");
				result = true;
				break;
			}
		} catch (java.net.MalformedURLException e) {
			System.out.println("網址格式錯誤!!!");
			e.printStackTrace();
		} catch (java.io.IOException e) {
			System.out.println("連線有問題!!!!!!");
			e.printStackTrace();
		}
		return result;
	}
}
