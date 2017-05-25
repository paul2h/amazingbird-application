package com.wavegis.engin.image.cctv.mjpeg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class CCTVMutiThreadEngin extends TimerEngin {

	public static final String enginID = "CCTVMuti";
	private static final String enginName = "CCTV多功讀取Engin";
	private static final CCTVMutiThreadEnginView enginView = new CCTVMutiThreadEnginView();
	private Logger logger;

	private ConcurrentLinkedQueue<CCTVData> cctvQueue = new ConcurrentLinkedQueue<>();
	private Thread[] picGettingThreads = new Thread[Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("CCTVThreadNumber"))];

	public CCTVMutiThreadEngin() {
		setTimeout(Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod")));
		logger = LogTool.getLogger(CCTVMutiThreadEngin.class.getName());
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
	public void timerAction() {
		final String imageExtension = "jpg";
		cctvQueue.clear();
		cctvQueue.addAll(GlobalConfig.CCTV_DATA_LIST);
		for (int i = 0; i < picGettingThreads.length; i++) {
			if (picGettingThreads[i] == null) {
				picGettingThreads[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						String threadName = Thread.currentThread().getName();
						CCTVData cctvData;
						while (isStarted()) {
							if ((cctvData = cctvQueue.poll()) != null) {

								showMessage(threadName + " : " + cctvData.getStname() + "...");
								String imagePath = cctvData.getSavePath();
								String ip = cctvData.getIp();
								String url_suffix = cctvData.getUrl_suffix();
								String account = cctvData.getAccount();
								String password = cctvData.getPassword();

								if (ip == null || ip.trim().isEmpty()) {
									showMessage(threadName + " : " + "IP資料有誤");
									continue;
								} else if (url_suffix == null || url_suffix.trim().isEmpty()) {
									showMessage(threadName + " : " + "URL資料有誤");
									continue;
								} else if (account == null || account.trim().isEmpty()) {
									showMessage(threadName + " : " + "帳號資料有誤");
									continue;
								} else if (password == null || password.trim().isEmpty()) {
									showMessage(threadName + " : " + "密碼資料有誤");
									continue;
								}
								if (imagePath == null || imagePath.length() == 0) {
									imagePath = GlobalConfig.XML_CONFIG.getProperty("CCTVImagePath") + cctvData.getStid() + "." + imageExtension;
								}

								String urlString = "http://" + ip.trim() + url_suffix.trim();
								try {
									showMessage(threadName + " : " + "開始連線..." + urlString);

									// #[[ TODO 暫時把連線放在這 待修正
									URL url;
									url = new URL(urlString);
									HttpURLConnection connection = null;
									// 設定帳密並加密
									String authStr = account + ":" + password;
									byte[] bytesEncoded = Base64.encodeBase64(authStr.getBytes());
									String authEncoded = new String(bytesEncoded);
									connection = (HttpURLConnection) url.openConnection();
									connection.setConnectTimeout(5000);
									connection.setRequestProperty("Authorization", "Basic " + authEncoded);

									BufferedImage image = ImageIO.read(connection.getInputStream());
									ImageIO.write(image, "jpg", new File(imagePath));
									// ]]

									// #[[ 圖片增加時間文字
									File file = new File(imagePath);
									int imageWidth = image.getWidth(null);
									int imageHeight = image.getHeight(null);
									BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
									Graphics2D g2d = bufferedImage.createGraphics();
									String text = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

									g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
									g2d.setColor(Color.YELLOW);
									g2d.setFont(new Font("標楷體", Font.BOLD, 24));
									g2d.drawString(text, 30, 25);

									ImageIO.write(bufferedImage, "jpg", file);
									// ]]
									showMessage(threadName + " : " + cctvData.getStname() + " 影像取得成功.");

								} catch (IOException e) {
									e.printStackTrace();
									
									try {
										ImageIO.write(ImageIO.read(new URL("http://13.76.255.253/cctv_error.jpg")), "jpg", new File(imagePath));
									} catch(IOException e1){
										e1.printStackTrace();
									} finally {
										showMessage(threadName + " : " + cctvData.getStname() + " 影像取得失敗.");
									}
								}
							} else {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}

				});
				picGettingThreads[i].start();
			}
		}
	}

	@Override
	public synchronized void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
