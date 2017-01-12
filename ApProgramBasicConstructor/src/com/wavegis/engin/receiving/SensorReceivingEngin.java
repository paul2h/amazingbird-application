package com.wavegis.engin.receiving;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class SensorReceivingEngin implements Engin{
	private static final String enginID = "SensorReceiving";
	private static final String enginName = "建驊水位雨量站Engin";
	private static final SensorReceivingEnginView enginView = new SensorReceivingEnginView();
	
	public static boolean isStarted;
	
	private ExecutorService executorService;
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private SensorReceivingEngin engin;
	private ServerSocket serverSocket;
	
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
	
	public void showMessage(String message){
		enginView.showMessage(message);
		logger.info(message);
	}

	@Override
	public boolean isStarted(){
		return isStarted;
	}

	@Override
	public boolean startEngin(){
		showMessage("準備開啟Engin...");
		
		isStarted = true;
		engin = this;
		
		sensorReceiving();
		
		showMessage("Engin已啟動");
		
		return isStarted;
	}

	@Override
	public boolean stopEngin(){
		showMessage("準備停止Engin");
		
		isStarted = false;
		if(executorService != null && !executorService.isShutdown()){
			executorService.shutdown();
			executorService = null;
		}
		if(serverSocket != null){
			try {
				serverSocket.close();
				serverSocket = null;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		if((executorService == null || executorService.isShutdown()) && serverSocket == null){
			showMessage("Engin已停止");
		} else {
			showMessage("Engin停止失敗");
		}
		
		return isStarted;
	}
	
	private void sensorReceiving(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					if(serverSocket == null){
						int serverPort = Integer.parseInt(GlobalConfig.XML_CONFIG.getProperty("SensorReceivingPort", "9999"));
						
						serverSocket = new ServerSocket(serverPort);
						
						showMessage("開始接收資料...");
					}
					while(isStarted){
    					Socket socket = serverSocket.accept();
    					
    					if (executorService == null) {
    						executorService = Executors.newCachedThreadPool();
    						
    						executorService.execute(new DataReceiving(engin, socket));
    					}
    					
					}
				} catch(IOException e){
					e.printStackTrace();
				}
			}
			
		}).start();
	}
}
