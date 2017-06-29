package com.wavegis.engin.image.trans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;

public class ImageTransClientEngin extends TimerEngin {

	public static final String enginID = "ImageTransClient";
	private static final String enginName = "圖片傳輸ClientEngin";
	private static final ImageTransClientEnginView enginView = new ImageTransClientEnginView();

	private Logger logger = LogTool.getLogger(ImageTransClientEngin.class.getName());

	String[] fileNames = { "1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg", "6.jpg" };// TODO 待完善
	private String dirPath = "D:\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\";//TODO 待完善
	
	public ImageTransClientEngin() {
		setTimeout(1000 * 10);
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
		Socket socket = null;

		for (int i = 0; i < 6; i++) {
			try {
				socket = new Socket("13.76.255.253", 5000);// TODO
				OutputStream out = socket.getOutputStream();
				
				File file = new File(dirPath+fileNames[i]);
				showMessage("傳送圖片 : " + file.getAbsolutePath());
				byte[] bytes = new byte[16 * 1024];
				InputStream in = new FileInputStream(file);

				int count;
				while ((count = in.read(bytes)) > 0) {
					out.write(bytes, 0, count);
				}
				in.close();
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		showMessage("傳送完成.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
