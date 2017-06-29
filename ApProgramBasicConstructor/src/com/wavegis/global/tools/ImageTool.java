package com.wavegis.global.tools;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageTool {
	
	public static void main(String[] args) {
		ImageTool.saveScreemshot(0, 0, 200, 200, "E:\\temp\\test.jpg");
	}

	private static Robot robot;

	/** 螢幕截圖 */
	public static synchronized boolean saveScreemshot(int x , int y , int width , int height , String outputImagePath) {
		boolean isSuccess = false;

		try {
			if (robot == null) {
				robot = new Robot();
			}
			Rectangle rect = new Rectangle(x, y, width, height);
			BufferedImage bufferedImage = robot.createScreenCapture(rect);
			ImageIO.write(bufferedImage, "jpg", new File(outputImagePath));
			
		} catch (AWTException e) {
			e.printStackTrace();
			isSuccess = false;
		} catch (Exception e) {

		}
		return isSuccess;

	}

}
