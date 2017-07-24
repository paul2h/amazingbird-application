package com.wavegis.global.firebase.realtime_db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataBase {
	
	private static DataBase instance = null;

	private static DatabaseReference database;

	private static final String DATABASE_URL = "https://beaming-ion-547.firebaseio.com/";
	private static final String SERVICE_ACCOUNT_CREDENTIALS = "./conf/beaming-ion-547-firebase-adminsdk-qdfty-15d29b6336.json";

	private List<Map<String, ChildEventListener>> childListeners = new ArrayList<>();

	public static DataBase getInstance() { 
        if (instance == null) {
            instance = new DataBase(); 
        }
        return instance; 
    }
	
	private DataBase() {

		try {
			// [START initialize]
			FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_CREDENTIALS);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount)).setDatabaseUrl(DATABASE_URL)
					.build();
			FirebaseApp.initializeApp(options);
			// [END initialize]
		} catch (IOException e) {
			System.out.println("ERROR: invalid service account credentials. See README.");
			System.out.println(e.getMessage());

			System.exit(1);
		}

		database = FirebaseDatabase.getInstance().getReference();
	}
	
	public void update(String child, String key, Object obj) {
		final DatabaseReference ref = database.child(child).child(key);
		ref.setValue(obj);
	}

	public void append(String child, Object obj) {
		final DatabaseReference ref = database.child(child).push();
		ref.setValue(obj);
	}

	@SuppressWarnings("serial")
	public void addListener(String child, Handler handler) {
		ChildEventListener cel = new ChildEventListener() {

			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
				handler.add(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				handler.remove(arg0);
			}
		};
		
		database.child(child).addChildEventListener(cel);
		Map<String, ChildEventListener> celMap = new HashMap<String, ChildEventListener>() {{put(child, cel);}};
		childListeners.add(celMap);
	}
	
	public void removeListenersWithChild(String child) {
		List<ChildEventListener> ces = childListeners.stream().filter(celMap -> celMap.containsKey(child))
															  .map(celMap -> celMap.get(child))
															  .collect(Collectors.toList());
		ces.stream().forEach(ce -> database.child(child).removeEventListener(ce));
		childListeners = childListeners.stream().filter(celMap -> !celMap.containsKey(child))
												.collect(Collectors.toList());
		System.out.println("after childListeners size():" + childListeners.size());
	}
	
	public void removeAllListener() {
		for (Map<String, ChildEventListener> celMap : childListeners) {
			String childOfMap = (String) celMap.keySet().toArray()[0];
			ChildEventListener celOfMap = (ChildEventListener) celMap.values().toArray()[0];
			database.child(childOfMap).removeEventListener(celOfMap);
		}
		childListeners.clear();
	}

}
