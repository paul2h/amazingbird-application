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
import com.wavegis.global.tools.LogTool;

/**
 * <pre>
 * 將圖片從資料夾移到另一個指定資料夾,並打上日期
 * 
 * (CCTV使用)
 * </pre>
 * 
 * 圖片檔日期格式 : 編號四碼數字+yyMMdd_HHmmss.jpg<br>
 * Ex:0001161019_213000.jpg
 *
 */
public class ImageEngin extends TimerEngin {
	private static final String enginID = "Image";
	private static final String enginName = "Image讀取Engin";
	private static final ImageEnginView enginView = new ImageEnginView();
	private Logger logger;

	public ImageEngin() {
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

	private String ImageDirPath = "D://temp//";// TODO 待設定
	private String ImageNewDirPath = "D://temp//";// TODO 待設定

	@Override
	public void timerAction() {
		showMessage(" 開始取得圖片...");
		File dir = new File(ImageDirPath);

		if (!dir.exists() || !dir.isDirectory()) {
			showMessage("圖片存放資料夾有誤.");
			return;
		}
		File[] images = dir.listFiles();
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");

		// #[[ 找出資料夾內各個 ID 最新的圖片檔案
		for (File image : images) {
			if (!image.exists() || !image.isFile()) {
				continue;
			}
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
		if (map.size() > 0) {
			File newDir = new File(ImageNewDirPath);

			if (!newDir.exists()) {
				newDir.mkdirs();
			}
		}
		// #[[ 搬移圖片到指定資料夾
		for (String key : map.keySet()) {
			// #[[ 圖片增加時間文字
			try {
				Date imageDate = (Date) map.get(key);
				String dateStr = sdf.format(imageDate);
				Image image = ImageIO.read(new File(ImageDirPath + key + dateStr + ".jpg"));

				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				String text = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(imageDate);

				g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
				g2d.setColor(Color.YELLOW);
				g2d.setFont(new Font("標楷體", Font.BOLD, 24));
				g2d.drawString(text, 30, 25);

				if (ImageIO.write(bufferedImage, "jpg", new File(ImageNewDirPath + (Long.valueOf(key)) + ".jpg"))) {
					try {
						// 刪除同ID 檔案
						Runtime.getRuntime().exec("cmd /c del " + ImageDirPath.replace("/", "\\") + key + "*");
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
		showMessage("圖片取得結束.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}
}
