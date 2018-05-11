package com.wavegis.engin.connection.tcp.nio_socket.lockist;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class LockistReceiveEngineView extends SimpleEnginView{
	
	private static final String enginID = LockistReceiveEngine.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}
	
}
