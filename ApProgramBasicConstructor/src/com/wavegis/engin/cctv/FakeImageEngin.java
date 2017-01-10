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
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.tools.LogTool;

public class FakeImageEngin extends TimerEngin {

	public static final String enginID = "FakeImage";
	private static final String enginName = "假圖片Engin";
	private static final FakeImageEnginView enginView = new FakeImageEnginView();
	private Logger logger = LogTool.getLogger(FakeImageEngin.class.getName());

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy E HH:mm:ss", Locale.US);

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

	private String fakeImageDir = "D://temp//CCTV//";// TODO 待設定
	private String imageSaveDir = "D://temp//CCTV//fake//";// TODO 待設定

	@Override
	public void timerAction() {
		showMessage(" 開始取得圖片...");
		File dir = new File(fakeImageDir);

		if (!dir.exists() || !dir.isDirectory()) {
			showMessage("圖片存放資料夾有誤.");
			return;
		}
		File[] images = dir.listFiles();

		// #[[ 找出資料夾內各個 ID 最新的圖片檔案
		for (File imageFile : images) {
			if (!imageFile.exists() || !imageFile.isFile()) {
				continue;
			}
			// ex : kin_1.jpg
			String filename = imageFile.getName();
			String stid = filename.split("_")[0];
			String jpgName = filename.split("_")[1];
			String outputImagePath = imageSaveDir + jpgName;

			showMessage("stid = " + stid);
			// #[[ 圖片增加時間文字
			try {
				Date imageDate = new Date();
				Image image = ImageIO.read(new File(fakeImageDir + filename));

				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				String text = sdf.format(imageDate);

				g2d.drawImage(image, 0, 0, imageWidth, imageHeight, null);
				g2d.setColor(Color.YELLOW);
				g2d.setFont(new Font("arial", Font.BOLD, 62));
				g2d.drawString(text, 100, 75);

				ImageIO.write(bufferedImage, "jpg", new File(outputImagePath));

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// ]]
		}
		// ]]

		// #[[ 搬移圖片到指定資料夾
		// ]]
		showMessage("圖片取得結束.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
