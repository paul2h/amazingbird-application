package com.wavegis.global.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

/**
 * 透過HTTP 取得圖片
 */
public class HttpImageTool {

	/**
	 * 最基礎簡單的取圖片方式
	 * 
	 * @throws IOException
	 */
	public static void getImage(String urlString, String saveImagePath) throws IOException {
		URL url = new URL(urlString);
		url.openConnection();
		BufferedImage image = ImageIO.read(url);
		ImageIO.write(image, "jpg", new File(saveImagePath));
		System.out.println("finished");
	}

	/**
	 * 取得需要帳密的圖片
	 * 
	 * @param urlString
	 * @param username
	 * @param password
	 * @param saveImagePath
	 * @throws IOException
	 */
	public static void getAuthorizedImage(String urlString, String username, String password, String saveImagePath) throws IOException {
		URL url;

		url = new URL(urlString);

		HttpURLConnection connection = null;
		// 設定帳密並加密
		String authStr = username + ":" + password;
		byte[] bytesEncoded = Base64.encodeBase64(authStr.getBytes());
		String authEncoded = new String(bytesEncoded);
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestProperty("Authorization", "Basic " + authEncoded);

		BufferedImage image = ImageIO.read(connection.getInputStream());
		ImageIO.write(image, "jpg", new File(saveImagePath));

	}

	/** 取得flash檔 (未完成) */
	public static void getFlashFile() throws IOException {
		URL url = new URL("http://fhy.wra.gov.tw/PUB_WEB_2011/Flash/reservoir/ReservoirWarning.swf");
		// url.openConnection();
		InputStream is = url.openStream();
		FileOutputStream fos = new FileOutputStream("D:/test.swf");
		while (true) {
			int i = is.read();
			if (i == -1) {
				break;
			}
			fos.write(i);
		}
		is.close();
		fos.close();
		System.out.println("finished");
	}

}
