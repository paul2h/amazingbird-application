package com.wavegis.global.firebase.realtime_db;

import com.google.firebase.database.DataSnapshot;

public interface Handler {
	
    public void add(DataSnapshot dataSnapshot);
    
    public void remove(DataSnapshot dataSnapshot);


}
