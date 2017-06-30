package com.wavegis.engin.image.cctv.hsinchu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class HsinchuCCTVEngin extends TimerEngin{
	private static final String enginID = "HsinchuCCTV";
	private static final String enginName = "新竹CCTV";
	private static final HsinchuCCTVEnginView enginView = new HsinchuCCTVEnginView();
	
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public HsinchuCCTVEngin(){
		setTimeout(GlobalConfig.XML_CONFIG.getProperty("CCTV_Time_Period"));
	}
	
	@Override
	public String getEnginID(){
		return enginID;
	}

	@Override
	public String getEnginName(){
		return enginName;
	}

	@Override
	public EnginView getEnginView(){
		return enginView;
	}

	@Override
	public void timerAction(){
		showMessage("開始取得CCTV圖片...");
		
		String[] cctvURLs = GlobalConfig.XML_CONFIG.getProperty("HsinchuCCTVURL").split(",");
		String[] imageNames = GlobalConfig.XML_CONFIG.getProperty("HsinchuImageName").split(",");
		String[] locationNames = GlobalConfig.XML_CONFIG.getProperty("HsinchuLocationName").split(",");
		
		if(cctvURLs.length != imageNames.length && imageNames.length != locationNames.length){
			showMessage("設定檔取得失敗，資料筆數不對等");
			
			return ;
		}
		String imageDirPath = GlobalConfig.XML_CONFIG.getProperty("CCTVImagePath");
		File dir = new File(imageDirPath);
		
		if(!dir.exists()){
			dir.mkdirs();
		}
		try {
			for(int i = 0 ; i < cctvURLs.length ; i++){
				BufferedImage image = ImageIO.read(new URL(cctvURLs[i]));
				String imagePath = imageDirPath + imageNames[i];
				String result = "失敗";
				
				if(ImageIO.write(image, "jpg", new File(imagePath))){
					result = "完畢";
				}
				showMessage(locationNames[i] + " 圖片取得" + result);
			}
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		showMessage("CCTV圖片取得完畢.");
	}

	@Override
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}
}