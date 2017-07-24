package com.wavegis.engin.db.flood_check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.google.firebase.database.DataSnapshot;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.ProxyData;
import com.wavegis.global.firebase.cloud_message.PushNotification;
import com.wavegis.global.firebase.realtime_db.DataBase;
import com.wavegis.global.firebase.realtime_db.Handler;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.fcm.PushInfo;
import com.wavegis.model.flood.Device;
import com.wavegis.model.flood.FloodAlertData;
import com.wavegis.model.water.WaterAlertData;

public class FloodAlertEngin implements Engin {
	
	private int timerPeriod = 1000;// 預設一秒鐘一次

	public static final String enginID = "floodAlert";
	private static final String enginName = "淹水範圍監控1.0";
	private boolean isStarted = false;
	
    private static final String CHILD_ALERT = "alert_changhua";
    private static final String CHILD_DEVICE = "device_changhua";
	
    private Map<String, Device> devices = new HashMap<>();
	
	private TimerTask timerTask;
	private PushNotification pushNotification = new PushNotification();
	
	private Logger logger;
	private static final FloodAlertEnginView enginView = new FloodAlertEnginView();

	public FloodAlertEngin() {
		
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
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public boolean startEngin() {	
		DataBase db = DataBase.getInstance();
		
		showMessage("Add " + CHILD_DEVICE + " listener");
		db.addListener(CHILD_DEVICE, new Handler() {
			@Override
			public void add(DataSnapshot dataSnapshot) {
	  	    	System.out.println("listeners:");
	  	    	String key = dataSnapshot.getKey();
                final Device device = dataSnapshot.getValue(Device.class);
                
  	    		showMessage("Get device token:" + device.getFcm_token() + " os:" + device.getDevice_os());
                devices.put(key, device);
			}

			@Override
			public void remove(DataSnapshot dataSnapshot) {
				
			}
		});
		
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if(devices.size() == 0) return;
				if(ProxyData.FCM_FLOOD_ALERT_SEND_LIST.size() != 0) {
					checkAndSendFloodAlert();
				}
				if(ProxyData.FCM_WATERLEVEL_ALERT_SEND_LIST.size() != 0) {
					checkAndSendWaterAlert();
				}
			}

			private void checkAndSendFloodAlert() {
			    System.out.println("checkAndSendFloodAlert");

				Map<String, FloodAlertData> fcmFloodMap = ProxyData.FCM_FLOOD_ALERT_SEND_LIST;
				for (Map.Entry<String, FloodAlertData> entry : fcmFloodMap.entrySet())
				{
					FloodAlertData fad = entry.getValue();
				    System.out.println("fad isHasSend:" + fad.isHasSend());
				    if (!fad.isHasSend()) {
		                String title = "水位站警戒:";
				    	String body = "(" + fad.getStid() + ")" +
									  "﹐請相關人員即時因應!" +
									  "time:" + fad.getDatatime_long() +
									  "範圍:(" + fad.getArea_range() + ")" +
									  "水深:(" + fad.getWaterlevel() + ")";
				    	onSendAlertMessage(title, body, "flood", fad.getStid());
				    	fad.setHasSend(true);
					}
				}
			}
			
			private void checkAndSendWaterAlert() {
			    System.out.println("checkAndSendWaterAlert");

				Map<String, WaterAlertData> fcmFloodMap = ProxyData.FCM_WATERLEVEL_ALERT_SEND_LIST;
				for (Map.Entry<String, WaterAlertData> entry : fcmFloodMap.entrySet())
				{
					WaterAlertData wad = entry.getValue();
				    System.out.println("wad HasSend:" + wad.HasSend());
				    if (!wad.HasSend()) {
		                String title = "水位站警戒:";
				    	String body = "(" + wad.getStnm() + ")" +
									  "﹐請相關人員即時因應!" +
									  "time:" + wad.getDatatime() +
									  "警戒值:(" + wad.getAlert_value() + ")" +
									  "水位:(" + wad.getWaterlv() + ")";
						onSendAlertMessage(title, body, "waterlv", wad.getStid());
						wad.setHasSend(true);
					}
				}
			}

			private void onSendAlertMessage(String title, String body, String type, String floodID) {
				Map<String, Object> datas = new HashMap<String, Object>() {
				    {
				    	put("title", title);
				    	put("content", body);
				        put("type", type);
				        put("floodID", floodID);
				    }
				};
				List<String> iosTokens = devices.values().stream().filter(d -> d.getDevice_os().equals("iOS"))
			   	 						 						  .map(d -> d.getFcm_token())
			   	 						 						  .collect(Collectors.toList());

				List<String> androidTokens = devices.values().stream().filter(d -> d.getDevice_os().equals("Android"))
														   	 		  .map(d -> d.getFcm_token())
														   	 		  .collect(Collectors.toList());

				PushInfo iOSPushInfo = new PushInfo(title, body, "iOS", iosTokens, datas);
				PushInfo androidPushInfo = new PushInfo(title, body, "Android", androidTokens, datas);
				
				showMessage("Send iOS device...");
				pushNotification.send(iOSPushInfo);
				showMessage("Send Android device...");
				pushNotification.send(androidPushInfo);
			}
		};
		new Timer().schedule(timerTask, 1000, 5000);
		
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("Remove all listeners");
		DataBase db = DataBase.getInstance();
		db.removeListenersWithChild(CHILD_DEVICE);
		db.removeListenersWithChild(CHILD_ALERT);
		boolean stopSuccess = false;
		if (timerTask != null) {
			stopSuccess = timerTask.cancel();
			timerTask = null;
		}
		isStarted = false;
		return stopSuccess;
	}
	
	private void showMessage(String message) {
		enginView.showMessage(message);
		if (logger == null) {
			logger = LogTool.getLogger(FloodAlertEngin.class.getName());
		}
		logger.info(message);
	}
}
