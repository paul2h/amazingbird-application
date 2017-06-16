package com.wavegis.engin.image.cctv.screem_save;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.ImageTool;
import com.wavegis.global.tools.LogTool;

public class ScreemSaveEngin extends TimerEngin {

	public static final String enginID = "ScreemSave";
	private static final String enginName = "螢幕截圖";
	private static final EnginView enginView = new ScreemSaveEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());

	
	public ScreemSaveEngin() {
		setTimeout(1000 * 30);
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
		showMessage("開始擷取螢幕畫面...");
		String imagePath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/yilan_cctv/1.jpg"; // TODO 功能未完成 目前將螢幕分四塊擷取
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		ImageTool.saveScreemshot(0, 0, dimension.width / 2, dimension.height / 2, imagePath);
		showMessage("擷取成功 : " + imagePath);

		imagePath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/yilan_cctv/2.jpg";
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		ImageTool.saveScreemshot(dimension.width / 2, 0, dimension.width / 2, dimension.height / 2, imagePath);
		showMessage("擷取成功 : " + imagePath);

		imagePath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/yilan_cctv/3.jpg";
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		ImageTool.saveScreemshot(0, dimension.height / 2, dimension.width / 2, dimension.height/2, imagePath);
		showMessage("擷取成功 : " + imagePath);

		imagePath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/yilan_cctv/4.jpg";
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		ImageTool.saveScreemshot(dimension.width / 2, dimension.height / 2, dimension.width/2, dimension.height/2, imagePath);
		showMessage("擷取成功 : " + imagePath);

		showMessage("擷取螢幕畫面完成.");
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
