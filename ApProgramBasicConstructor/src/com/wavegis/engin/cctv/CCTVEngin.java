package com.wavegis.engin.cctv;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.CCTVData;

public class CCTVEngin extends TimerEngin {

	private static final String enginID = "CCTV";
	private static final String enginName = "CCTV讀取Engin";
	private static final CCTVEnginView enginView = new CCTVEnginView();
	private Logger logger;

	private Thread picGettingThread;

	public CCTVEngin() {
		setTimeout(Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod")));
		logger = LogTool.getLogger(CCTVEngin.class.getName());
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

	@SuppressWarnings("deprecation")
	@Override
	public void timerAction() {
		final String imageExtension = "jpg";

		if (picGettingThread != null && picGettingThread.isAlive()) {
			showMessage("上次未跑完,殺掉Thread");
			picGettingThread.destroy();// TODO 待找方法修改
		}
		picGettingThread = null;

		
		picGettingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				showMessage("開始取得影像...");
				for (CCTVData cctvData : GlobalConfig.CCTV_DATA_LIST) {
					String imagePath = cctvData.getSavePath();
					String ip = cctvData.getIp();
					String url_suffix = cctvData.getUrl_suffix();
					String account = cctvData.getAccount();
					String password = cctvData.getPassword();

					if (ip == null || ip.trim().isEmpty()) {
						continue;
					} else if (url_suffix == null || url_suffix.trim().isEmpty()) {
						continue;
					} else if (account == null || account.trim().isEmpty()) {
						continue;
					} else if (password == null || password.trim().isEmpty()) {
						continue;
					}
					if (imagePath == null || imagePath.length() == 0) {
						imagePath = GlobalConfig.XML_CONFIG.getProperty("CCTVImagePath") + cctvData.getStid() + "." + imageExtension;
					}
					showMessage(cctvData.getStname() + "...");

					String url = "http://" + ip.trim() + url_suffix.trim();
					try {
						HttpImageTool.getAuthorizedImage(url, account, password, imagePath);
						// #[[ 圖片增加時間文字
						File file = new File(imagePath);
						Image image = ImageIO.read(file);
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
						showMessage(cctvData.getStname() + " 影像取得成功.");
					} catch (IOException e) {
						showMessage(cctvData.getStname() + " 影像取得失敗.");
						e.printStackTrace();
					}
				}
				showMessage("影像取得結束.");
			}
		});
		picGettingThread.start();
		
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
