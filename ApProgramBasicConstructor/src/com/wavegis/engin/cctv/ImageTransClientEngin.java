package com.wavegis.engin.cctv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.EnginView;
import com.wavegis.engin.TimerEngin;
import com.wavegis.global.tools.LogTool;

public class ImageTransClientEngin extends TimerEngin {

	public static final String enginID = "ImageTransClient";
	private static final String enginName = "圖片傳輸ClientEngin";
	private static final ImageTransClientEnginView enginView = new ImageTransClientEnginView();

	private Logger logger = LogTool.getLogger(ImageTransClientEngin.class.getName());

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
				socket = new Socket("127.0.0.1", 9999);// TODO
				OutputStream out = socket.getOutputStream();

				File file = new File("E:\\test.jpg");
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
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
