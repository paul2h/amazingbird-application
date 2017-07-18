package com.wavegis.global.firebase.realtime_db;

import com.google.firebase.database.DataSnapshot;

public interface Handler {
	
    public void process(DataSnapshot dataSnapshot);

}
