package com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans.model.RemoteEnginData;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class RawDataSendEngin extends TimerEngin {

	public static final String enginID = "RawDataSend";
	private static final String enginName = "原始資料傳送";
	private static RawDataSendEnginView enginView = new RawDataSendEnginView();
	private static Logger logger = LogTool.getLogger(RawDataSendEngin.class.getName());

	private Thread connectionListenerThread;
	private ServerSocketChannel serverChannel;
	private int rawDataSendPort = Integer.valueOf(GlobalConfig.XML_CONFIG.get("RawDataSendPort").toString());

	private ConcurrentHashMap<String, RemoteEnginData> SEND_ENGINs = new ConcurrentHashMap<>();
	private ConcurrentLinkedQueue<String> RAW_DATA_QUEUE = new ConcurrentLinkedQueue<>();

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
	public boolean startEngin() {
		connectionListenerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Selector selector;
					InetSocketAddress listenAddress;

					String address = "0.0.0.0";// 0.0.0.0為本機端

					showMessage("開啟Send Port :" + address + ":" + rawDataSendPort);
					listenAddress = new InetSocketAddress(address, rawDataSendPort);
					showMessage("準備開啟連接口");
					selector = Selector.open();
					serverChannel = ServerSocketChannel.open();
					serverChannel.configureBlocking(false);// 此行待了解

					// retrieve server socket and bind to port
					serverChannel.socket().bind(listenAddress);
					serverChannel.register(selector, SelectionKey.OP_ACCEPT);

					showMessage("Sender連接口開啟完成.");

					while (isStarted()) {
						selector.select();// 會停在這等連線 or 訊息

						Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
						while (keys.hasNext()) {
							SelectionKey key = (SelectionKey) keys.next();
							// 防止讀取到同樣的key 把這個key先刪除
							keys.remove();

							if (!key.isValid()) {
								continue;
							}

							if (key.isAcceptable()) { // 連接key
								try {
									accept(selector, key);
								} catch (Exception e) {
									showMessage("連線錯誤!");
									key.channel().close();
								}
							} else if (key.isReadable()) { // 訊息key
								try {
									read(key);
								} catch (Exception e) {
									showMessage("連線錯誤!" + ((SocketChannel) key.channel()).getRemoteAddress());
									key.channel().close();
								}
							}
						}
					}

					closeListenSocket(serverChannel, selector);
				} catch (Exception e) {
					showMessage("接收問題  停止接收  " + e.getMessage());
					e.printStackTrace();
				}

			}
		});
		connectionListenerThread.start();
		return super.startEngin();
	}

	@Override
	public void timerAction() {
		String rawData;
		while ((rawData = RAW_DATA_QUEUE.poll()) != null) {
			sendData(rawData);
		}
	}

	private void sendData(String rawData) {
		for (String ip : SEND_ENGINs.keySet()) {
			RemoteEnginData remoteEngin = SEND_ENGINs.get(ip);
			if (remoteEngin.isRegestered()) {
				SocketChannel channel = remoteEngin.getChannel();
				ByteBuffer buffer = ByteBuffer.wrap(rawData.getBytes());
				try {
					channel.write(buffer);
					showMessage("轉傳資料流 :" + channel.getRemoteAddress() + " - " + rawData);
					buffer.clear();
				} catch (IOException e1) {
					e1.printStackTrace();
					showMessage("資料流轉傳錯誤!自動強制斷線 =" + ip + ", 錯誤訊息 ：" + e1.getMessage());
					try {
						channel.close();
					} catch (IOException e) {
					}
					SEND_ENGINs.remove(ip);
				}
			}
		}
	}

	@Override
	public boolean stopEngin() {
		boolean result = super.stopEngin();
		// #[[ 做一次連線觸發關閉Nio Socket
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("0.0.0.0", rawDataSendPort));
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ]]
		return result;
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	// 與車機連線
	private void accept(Selector selector, SelectionKey key) throws IOException {

		// #[[ 連線 & Selector設定
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		SocketAddress remoteAddr = channel.getRemoteAddress();
		showMessage("接收到連線  " + remoteAddr.toString());
		channel.register(selector, SelectionKey.OP_READ);
		// ]]

		// #[[ 放入Engin清單中
		String ip = channel.getRemoteAddress().toString().split(":")[0];
		RemoteEnginData remoteEngin = new RemoteEnginData();
		remoteEngin.setChannel(channel);
		SEND_ENGINs.put(ip, remoteEngin);
		// ]]
	}

	// 讀取原始資料
	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {// 正常斷線key
			String ip = channel.getRemoteAddress().toString().split(":")[0];
			showMessage("斷線 - " + ip);
			SEND_ENGINs.remove(ip);
			channel.close();
			key.cancel();
			return;
		}

		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);
		String getMessage = new String(data).trim();
		showMessage("from " + channel.getRemoteAddress() + " : " + getMessage);
		buffer.clear();

		/* 收到註冊ID */
		if (getMessage.indexOf(GlobalConfig.XML_CONFIG.getProperty("RawDataTransRegisteredKey").toString()) >= 0) {// ex: "#ID#,WRA"
			String id = getMessage.split(",")[1];
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			try {
				String ip = remoteAddr.toString().split(":")[0];
				RemoteEnginData remoteEngin = SEND_ENGINs.get(ip);
				remoteEngin.setReceiverID(id);
				remoteEngin.setRegestered(true);// 註冊完成
				showMessage("註冊完成 : " + channel.getRemoteAddress() + "( ID =" + id);
				// 註冊完成 回傳完成訊息
				channel.write(ByteBuffer.wrap(GlobalConfig.XML_CONFIG.getProperty("RawDataTransRegisteredSuccessKey").toString().getBytes()));
			} catch (Exception e) {
			} // 若格式出問題就跳過 繼續執行
		}
		/* 收到測試訊息 回傳測試訊息 */
		if (getMessage.indexOf(GlobalConfig.XML_CONFIG.getProperty("RawDataTransTestConnectionKey").toString()) >= 0) {
			showMessage("收到測試連線訊息..." + getMessage);
			String returnMessage = GlobalConfig.XML_CONFIG.getProperty("RawDataTransTestConnectionKey").toString();
			showMessage("回傳訊息("+channel.getRemoteAddress()+")  : " + returnMessage);
			channel.write(ByteBuffer.wrap(returnMessage.getBytes()));
		}
	}

	private void closeListenSocket(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
		selector.close();
		serverSocketChannel.close();
	}

}
