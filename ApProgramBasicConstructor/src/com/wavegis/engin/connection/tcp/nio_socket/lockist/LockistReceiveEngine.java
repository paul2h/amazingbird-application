package com.wavegis.engin.connection.tcp.nio_socket.lockist;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.tools.LogTool;

public class LockistReceiveEngine implements Engin{
	
	public static final String enginID = "LockistReceive";
	private static final String enginName = "Lockist接收";
	private static final LockistReceiveEngineView enginView = new LockistReceiveEngineView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private InetSocketAddress listenAddress;
	
	private boolean isStarted = false;

	@Override
	public String getEnginID() {
		return enginID;
	}

	@Override
	public String getEnginName() {
		return enginName;
	}

	@Override
	public EnginView getEnginView() {
		return enginView;
	}

	@Override
	public boolean isStarted() {
		return isStarted;
	}

	@Override
	public boolean startEngin() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String address = "0.0.0.0";
					int port = 4000;// TODO

					showMessage("Create Server Address :" + address + ":" + port);
					listenAddress = new InetSocketAddress(address, port);
					showMessage("準備開啟連接口");

					selector = Selector.open();
					serverChannel = ServerSocketChannel.open();
					serverChannel.configureBlocking(false);// 此行待了解

					// retrieve server socket and bind to port
					serverChannel.socket().bind(listenAddress);
					serverChannel.register(selector, SelectionKey.OP_ACCEPT);
					showMessage("Server started...");
					while (true) {
						selector.select();// 會停在這等連線 or 訊息

						// work on selected keys
						Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
						while (keys.hasNext()) {
							SelectionKey key = (SelectionKey) keys.next();
							// this is necessary to prevent the same key from coming up
							// again the next time around.
							keys.remove();

							if (!key.isValid()) {
								continue;
							}

							if (key.isAcceptable()) { // 連接key
								try {
									accept(key);
								} catch (Exception e) {
									showMessage("有人斷線 - accept");
									key.channel().close();// 發現斷線則趕快中斷與他的連線 不然會報錯
								}
							} else if (key.isReadable()) { // 訊息key
								try {
									read(key);
								} catch (Exception e) {
									showMessage("有人斷線 - read");
									e.printStackTrace();
									key.channel().close();// 發現斷線則趕快中斷與他的連線 不然會報錯
								}
							}
						}
					}
				} catch (IOException e1) {
					showMessage("Receiver監聽結束");
					e1.printStackTrace();
				}
			}
		}).start();
		isStarted = true;
		return true;
	}



	@Override
	public boolean stopEngin() {
		isStarted = false;
		return false;
	}
	
	// accept a connection made to this channel's socket
	private void accept(SelectionKey key) throws IOException {
		showMessage("處理Client - accept");
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		showMessage("Connected to: " + remoteAddr);

		// register channel with selector for further IO
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	// read from the socket channel
	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {// 正常斷線key
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			showMessage("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return;
		}

		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);

		String getMessage = new String(data);
		showMessage("Got: " + getMessage);

		buffer.clear();
	}


	
	private void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
