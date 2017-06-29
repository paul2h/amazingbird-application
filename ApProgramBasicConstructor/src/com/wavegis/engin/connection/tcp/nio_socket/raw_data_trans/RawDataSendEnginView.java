package com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class RawDataSendEnginView extends SimpleEnginView{

	private static final String enginID = RawDataSendEngin.enginID;
	@Override
	public String getEnginID() {
		return enginID;
	}

}
