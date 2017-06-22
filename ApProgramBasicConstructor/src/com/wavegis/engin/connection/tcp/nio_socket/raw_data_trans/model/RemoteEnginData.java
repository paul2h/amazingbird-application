package com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans.model;

import java.nio.channels.SocketChannel;

public class RemoteEnginData {

	private String receiverID;
	private SocketChannel channel;
	private boolean regestered = false;

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public boolean isRegestered() {
		return regestered;
	}

	public void setRegestered(boolean regestered) {
		this.regestered = regestered;
	}

}
