package com.wavegis.engin.db.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import com.google.firebase.database.DataSnapshot;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.firebase.cloud_message.PushNotification;
import com.wavegis.global.firebase.realtime_db.DataBase;
import com.wavegis.global.firebase.realtime_db.Handler;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.fcm.PushInfo;
import com.wavegis.model.message.Message;
import com.wavegis.model.message.UserInfo;

public class MessageEngin implements Engin {
	
	public static final String enginID = "message";
	private static final String enginName = "警急應變即時通訊系統Engin";
	private boolean isStarted = false;
	
    private static final String CHILD_MESSAGE = "message_changhua";
    private static final String CHILD_USERINFO = "userInfo_changhua";

    private List<UserInfo> users = new ArrayList<>();
	
	private PushNotification pushNotification = new PushNotification();
	
	private Logger logger;
	private static final MessageEnginView enginView = new MessageEnginView();
	
	public MessageEngin() {
		
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
		
		showMessage("Add " + CHILD_USERINFO + " listener");
		db.addListener(CHILD_USERINFO, new Handler() {
			@Override
			public void process(DataSnapshot dataSnapshot) {
	  	    	System.out.println("listeners:");

                final UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                
  	    		showMessage("Get user:" + userInfo.getUser_name());
                users.add(userInfo);				
			}
		});
		
		showMessage("Add " + CHILD_MESSAGE + " listener");
		db.addListener(CHILD_MESSAGE, new Handler() {
			@Override
			public void process(DataSnapshot dataSnapshot) {
	  	    	//FCM Push
                final Message message = dataSnapshot.getValue(Message.class);
                String title = message.getName();
                String body = message.getText();
				Map<String, Object> datas = new HashMap<String, Object>() {
				    {
				    	put("title", title);
				    	put("message", body);
				        put("type", "message");
				    }
				};
				List<String> iosTokens = users.stream().filter(user -> user.getDevice_OS().equals("iOS"))
													   .map(user -> user.getFcm_token())
													   .collect(Collectors.toList());
				List<String> androidTokens = users.stream().filter(user -> user.getDevice_OS().equals("Android"))
														   .map(user -> user.getFcm_token())
														   .collect(Collectors.toList());
				
				PushInfo iOSPushInfo = new PushInfo(title, body, "iOS", iosTokens, datas);
				PushInfo androidPushInfo = new PushInfo(title, body, "Android", androidTokens, datas);

				pushNotification.send(iOSPushInfo);
				pushNotification.send(androidPushInfo);
			}
		});
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("Remove all child listeners");
		DataBase db = DataBase.getInstance();
		db.removeChildListenersWithChild(CHILD_USERINFO);
		db.removeChildListenersWithChild(CHILD_MESSAGE);
		isStarted = false;
		return false;
	}
	
	private void showMessage(String message) {
		enginView.showMessage(message);
		if (logger == null) {
			logger = LogTool.getLogger(MessageEngin.class.getName());
		}
		logger.info(message);
	}
}
