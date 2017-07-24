package com.wavegis.engin.db.message;

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
	private static final String enginName = "警急應變即時通訊系統1.0";
	private boolean isStarted = false;
	
    private static final String CHILD_MESSAGE = "message_changhua";
    private static final String CHILD_USERINFO = "userInfo_changhua";

    private Map<String, UserInfo> users = new HashMap<>();
	
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
			public void add(DataSnapshot dataSnapshot) {
	  	    	String key = dataSnapshot.getKey();
                final UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                
  	    		showMessage("Get user:" + userInfo.getUser_name() + " " + userInfo.getDevice_OS()+ " " + userInfo.getEmail()+ " " + userInfo.getFcm_token()+ " " + userInfo.getPhotoURL());
                users.put(key, userInfo);		
                System.out.println("users size:" + users.size());
			}

			@Override
			public void remove(DataSnapshot dataSnapshot) {
	  	    	String key = dataSnapshot.getKey();
                users.remove(key);
                System.out.println("users size:" + users.size());
			}
		});
		
		showMessage("Add " + CHILD_MESSAGE + " listener");
		db.addListener(CHILD_MESSAGE, new Handler() {
			@Override
			public void add(DataSnapshot dataSnapshot) {
	  	    	//FCM Push		
				
                final Message message = dataSnapshot.getValue(Message.class);
                if(message.isHasSend()) return;
                
                String key = dataSnapshot.getKey();
               
                String title = message.getUser_name();
                String body = message.getText();

				Map<String, Object> datas = new HashMap<String, Object>() {
				    {
				    	put("title", title);
				    	put("content", body);
				        put("type", "message");
				    }
				};
				
				List<UserInfo> sendUsers = users.values().stream().filter(user -> !message.getFrom_email().equals(user.getEmail()))
						   							   	 		  .collect(Collectors.toList());
								
				List<String> iosTokens = sendUsers.stream().filter(user -> user.getDevice_OS().equals("iOS"))
													   	   .map(user -> user.getFcm_token())
													   	   .collect(Collectors.toList());
				List<String> androidTokens = sendUsers.stream().filter(user -> user.getDevice_OS().equals("Android"))
														   	   .map(user -> user.getFcm_token())
														   	   .collect(Collectors.toList());

				PushInfo iOSPushInfo = new PushInfo(title, body, "iOS", iosTokens, datas);
				PushInfo androidPushInfo = new PushInfo(title, body, "Android", androidTokens, datas);

				pushNotification.send(iOSPushInfo);
				pushNotification.send(androidPushInfo);
				
				message.setHasSend(true);
				db.update(CHILD_MESSAGE, key, message);
			}

			@Override
			public void remove(DataSnapshot dataSnapshot) {
				
			}
			
		});
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("Remove all listeners");
		DataBase db = DataBase.getInstance();
		db.removeListenersWithChild(CHILD_USERINFO);
		db.removeListenersWithChild(CHILD_MESSAGE);
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
