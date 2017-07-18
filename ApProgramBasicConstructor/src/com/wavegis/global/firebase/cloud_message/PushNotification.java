package com.wavegis.global.firebase.cloud_message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pixsee.fcm.Message;
import org.pixsee.fcm.Notification;
import org.pixsee.fcm.Sender;

import com.wavegis.model.fcm.PushInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushNotification {
	
	private Sender fcm = new Sender("AAAAthTdmpg:APA91bEzC0hMWZIlIRRRDTgONoB6a7zUypcSpoS3zxRAPDBpbVc3k2E9pInW-5rmP79rwQj3WMYl-KzRT2X3p68843XUgcRCy9yI3DAdbbkzCe_-1Yjw_j0UOqnzy2y870pqIYnawSWK");
	
	public PushNotification() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public void send(PushInfo pushInfo) {
		Message message = pushInfo.genPushMessage();
		fcm.send(message, new Callback() {
			@Override
			public void onResponse(Call call, Response response) {
				if(response.isSuccessful())
					System.out.print("Hooray!");
			}

			@Override
			public void onFailure(Call call, Throwable t) {
				t.printStackTrace();
			}
		});
		
	}

	public static void main(String[] args) {
		PushNotification pn = new PushNotification();
		List<String> registrationIds = Arrays.asList("dav2AUROW-w:APA91bHpOYA8DyBrBq6yXJ4sJwoBVq0l84kXA8A9RWnOtl4-RBspXEFI_QLHGufFgfBL9n2cqklvdjhXmpYvAaYd-2Axn7wA54UEdDsZXe0lQ7pUvS0RGjXgZwUQE7jz2qF3GbdvF8WS");
		
		Map<String, Object> datas = new HashMap<String, Object>() {
		    {
		    	put("title", "wavegis");
		    	put("message", "test");
		        put("type", "message");
		    }
		};
		
		PushInfo pushInfo = new PushInfo("title", "body", "Android", registrationIds, datas);
		pn.send(pushInfo);
	}

}
