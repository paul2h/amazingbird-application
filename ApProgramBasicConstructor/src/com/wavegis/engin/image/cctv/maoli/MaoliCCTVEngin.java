package com.wavegis.engin.image.cctv.maoli;

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

public class MaoliCCTVEngin extends TimerEngin{
	private static final String enginID = "MaoliCCTV";
	private static final String enginName = "苗栗CCTV";
	private static final MaoliCCTVEnginView enginView = new MaoliCCTVEnginView();
	
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	public MaoliCCTVEngin(){
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
		
		String cctvURL = GlobalConfig.XML_CONFIG.getProperty("MaoliCCTVURL");
		String imageDirPath = GlobalConfig.XML_CONFIG.getProperty("CCTVImagePath");
		String[] imageNames = GlobalConfig.XML_CONFIG.getProperty("MaoliImageName").split(",");
		String[] locationNames = GlobalConfig.XML_CONFIG.getProperty("MaoliLocationName").split(",");
		
		if(imageNames.length != locationNames.length){
			showMessage("設定檔取得失敗，資料筆數不對等");
			
			return ;
		}
		File dir = new File(imageDirPath);
		
		if(!dir.exists()){
			dir.mkdirs();
		}
		try {
			for(int i = 0 ; i < imageNames.length ; i++){
				String imageName = imageNames[i];
				BufferedImage image = ImageIO.read(new URL(cctvURL + imageName));
				String imagePath = imageDirPath + imageName;
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
