package com.wavegis.engin.image.cctv.from_fold;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class ImageEngin extends TimerEngin {
	public static final String enginID = "Image";
	private static final String enginName = "Image讀取Engin";
	private static final ImageEnginView enginView = new ImageEnginView();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Logger logger;

	public ImageEngin() {
		setTimeout(Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("TimerPeriod_ImageEngin")));
		logger = LogTool.getLogger(ImageEngin.class.getName());
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
		showMessage(dateFormat.format(new Date()) + " 開始取得圖片...");

		// #[[ 找出資料夾內各個 ID 最新的圖片檔案
		File dir = new File(GlobalConfig.XML_CONFIG.getProperty("ImageDirPath"));

		if (!dir.exists() || !dir.isDirectory()) {
			showMessage("圖片存放資料夾有誤.");

			return;
		}
		File[] images = dir.listFiles();
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
		
		for (File image : images) {
			if (!image.exists() || !image.isFile()) {
				continue;
			}
			showMessage("讀取圖片 :" + image.getName());
			String filename = image.getName();
			int pointIndex = filename.lastIndexOf(".");
			// 檔名格式：xxxxYYMMDD_HHmmss.jpg
			if (pointIndex < 17) {
				continue;
			}
			String id = filename.substring(0, 4);
			String dateStr = filename.substring(4, pointIndex);
			Date date = (Date) map.get(id);
			Date imageDate = null;

			try {
				imageDate = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (date == null) {
				map.put(id, imageDate);
			} else if (date.before(imageDate)) {
				map.put(id, imageDate);
			}
		}
		// ]]

		// #[[ 搬移圖片到指定資料夾

		if (map.size() > 0) {
			File newDir = new File(GlobalConfig.XML_CONFIG.getProperty("ImageNewDirPath"));

			if (!newDir.exists()) {
				newDir.mkdirs();
			}
		}

		for (String key : map.keySet()) {
			// #[[ 圖片增加時間文字
			try {
				Date imageDate = (Date) map.get(key);
				String dateStr = sdf.format(imageDate);
				String imageFilePath = GlobalConfig.XML_CONFIG.getProperty("ImageDirPath") + key + dateStr + ".jpg";
				Image image = ImageIO.read(new File(imageFilePath));
				
				if(image == null){
					showMessage("圖片讀取失敗 : " + imageFilePath);
					continue;
				}
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				String text = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(imageDate);

				g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
				g2d.setColor(Color.YELLOW);
				g2d.setFont(new Font("標楷體", Font.BOLD, 24));
				g2d.drawString(text, 30, 25);

				if (ImageIO.write(bufferedImage, "jpg", new File(GlobalConfig.XML_CONFIG.getProperty("ImageNewDirPath") + (Long.valueOf(key)) + ".jpg"))) {
					try {
						// 刪除同ID 檔案
						Runtime.getRuntime().exec("cmd /c del " + GlobalConfig.XML_CONFIG.getProperty("ImageDirPath").replace("/", "\\") + key + "*");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// ]]
		}
		// ]]

		showMessage(dateFormat.format(new Date()) + " 圖片取得結束.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}
}
