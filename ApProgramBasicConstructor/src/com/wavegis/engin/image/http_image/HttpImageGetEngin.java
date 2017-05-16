package com.wavegis.engin.image.http_image;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.HttpImageTool;
import com.wavegis.global.tools.LogTool;

public class HttpImageGetEngin extends TimerEngin{
	
	public static final String enginID = "HttpImageGet";
	private static final String engninName = "Http圖片接收Engin";
	private static final HttpImageGetEnginView enginView = new HttpImageGetEnginView();
	private static Logger logger = LogTool.getLogger(HttpImageGetEngin.class.getName());

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return engninName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

	@Override
	public void timerAction() {
		String url = "http://im5.tongbu.com/ArticleImage/a1c2c7ed-b.jpg?w=480,343";
		String targetPath = "D://test.jpg";
		showMessage("開始擷取圖片..."+url);
		try {
			HttpImageTool.getImage(url, targetPath);		//TODO
			showMessage("擷取完成,已存入資料夾 " + targetPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
