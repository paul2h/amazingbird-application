package com.wavegis.engin.db.flood_check;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.firebase.database.DataSnapshot;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.firebase.cloud_message.PushNotification;
import com.wavegis.global.firebase.realtime_db.DataBase;
import com.wavegis.global.firebase.realtime_db.Handler;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.flood.Device;

public class FloodAlertEngin implements Engin {
	
	public static final String enginID = "floodAlert";
	private static final String enginName = "淹水範圍監控Engin";
	private boolean isStarted = false;
	
    private static final String CHILD_ALERT = "alert_changhua";
    private static final String CHILD_DEVICE = "device_changhua";
	
    private List<Device> devices = new ArrayList<>();
	
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
			public void process(DataSnapshot dataSnapshot) {
	  	    	System.out.println("listeners:");
                final Device device = dataSnapshot.getValue(Device.class);
                
  	    		showMessage("Get device token:" + device.getToken() + " os:" + device.getDevice_os());
                devices.add(device);				
			}
		});
		
		showMessage("Add " + CHILD_ALERT + " listener");
		db.addListener(CHILD_ALERT, new Handler() {
			@Override
			public void process(DataSnapshot dataSnapshot) {
	  	    	//FCM Push

			}
		});
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("Remove all child listeners");
		DataBase db = DataBase.getInstance();
		db.removeChildListenersWithChild(CHILD_DEVICE);
		db.removeChildListenersWithChild(CHILD_ALERT);
		isStarted = false;
		return false;
	}
	
	private void showMessage(String message) {
		enginView.showMessage(message);
		if (logger == null) {
			logger = LogTool.getLogger(FloodAlertEngin.class.getName());
		}
		logger.info(message);
	}
}
