package com.wavegis.engin.connection.tcp.nio_socket.kenkul;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class KenkulReceiveEnginView extends SimpleEnginView {

	private static final String enginID = KenkulLORAReceiveEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
