package com.wavegis.global.firebase.realtime_db;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.firebase.*;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;

public class DataBase {

	private static DatabaseReference database;

	private static final String DATABASE_URL = "https://beaming-ion-547.firebaseio.com/";
	private static final String SERVICE_ACCOUNT_CREDENTIALS = "./conf/beaming-ion-547-firebase-adminsdk-qdfty-15d29b6336.json";

	private ArrayList<Map<String, ValueEventListener>> valueListeners = new ArrayList<>();
	private ArrayList<Map<String, ChildEventListener>> childListeners = new ArrayList<>();

	public DataBase() {

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

	public void append(String child, Object obj) {
		final DatabaseReference ref = database.child(child).push();
		ref.setValue(obj);
	}

	@SuppressWarnings("serial")
	public void fetch(String child, Handler handler) {
		ValueEventListener vel = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				handler.process(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub

			}
		};
		
		database.child(child).addListenerForSingleValueEvent(vel);
		Map<String, ValueEventListener> velMap = new HashMap<String, ValueEventListener>() {{put(child, vel);}};
		valueListeners.add(velMap);
	}

	@SuppressWarnings("serial")
	public void addListener(String child, Handler handler) {
		ChildEventListener cel = new ChildEventListener() {

			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
				handler.process(dataSnapshot);
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

			}
		};
		
		database.child(child).addChildEventListener(cel);
		Map<String, ChildEventListener> celMap = new HashMap<String, ChildEventListener>() {{put(child, cel);}};
		childListeners.add(celMap);
	}
	
	public void removeAllListener() {
		removeValueListeners();
		removeChildListeners();
	}
	
	private void removeValueListeners() {
		for (Map<String, ValueEventListener> velMap : valueListeners) {
			String childOfMap = (String) velMap.keySet().toArray()[0];
			ValueEventListener velOfMap = (ValueEventListener) velMap.values().toArray()[0];
			database.child(childOfMap).removeEventListener(velOfMap);
		}
		valueListeners.clear();
	}
	
	private void removeChildListeners() {
		for (Map<String, ChildEventListener> celMap : childListeners) {
			String childOfMap = (String) celMap.keySet().toArray()[0];
			ChildEventListener celOfMap = (ChildEventListener) celMap.values().toArray()[0];
			database.child(childOfMap).removeEventListener(celOfMap);
		}
		childListeners.clear();
	}

}
