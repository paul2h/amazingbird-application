package com.wavegis.global.firebase;

import com.google.firebase.database.DataSnapshot;

public interface Handler {
	
    public void process(DataSnapshot dataSnapshot);

}
