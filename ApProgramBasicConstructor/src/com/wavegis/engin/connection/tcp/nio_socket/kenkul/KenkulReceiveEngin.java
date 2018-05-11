package com.wavegis.engin.connection.tcp.nio_socket.kenkul;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.AnalysisEngin;
import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.OriginalWaterData;
import com.wavegis.model.water.WaterData;

public class KenkulReceiveEngin implements Engin {

	public static final String enginID = "KenkulNormal";
	private static final String enginName = "塏固一般接收Engin";
	private static final KenkulReceiveEnginView enginView = new KenkulReceiveEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private boolean isStarted = false;

	private ServerSocketChannel serverChannel;
	private Selector selector;
	private InetSocketAddress listenAddress;
	private AnalysisEngin<OriginalWaterData<Double>> analysisEngin = new OriginalDataAnalysisEngin(OriginalDataAnalysisEngin.TYPE_Kenkul);
	private TimerTask timerTask;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

	/** 門號對應的防火牆號碼 */
	public static HashMap<String, String> STATION_WALL_ID_MAP = new HashMap<String, String>();
	/** 防火牆號碼對應的門號 */
	public static HashMap<String, String> STATION_NUMBER_ID_MAP = new HashMap<String, String>();

	public static Map<String, SocketChannel> STATION_SOCKET_MAP = new ConcurrentHashMap<String, SocketChannel>();

	public static ConcurrentLinkedQueue<WaterData> WATER_DATA_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	
	public KenkulReceiveEngin() {
		STATION_WALL_ID_MAP.put("0965171276", "003");// TODO
		STATION_WALL_ID_MAP.put("0965171286", "005");// TODO
		STATION_NUMBER_ID_MAP.put("003", "0965171276");// TODO
		STATION_NUMBER_ID_MAP.put("005", "0965171278");// TODO
	}

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
		try {
			String address = "0.0.0.0";
			int port = 9999;// TODO

			showMessage("Create Server Address :" + address + ":" + port);
			listenAddress = new InetSocketAddress(address, port);
			showMessage("準備開啟連接口");

			this.selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);// 此行待了解

			// retrieve server socket and bind to port
			serverChannel.socket().bind(listenAddress);
			serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
			startRequestTimer();
			showMessage("Server started...");
			while (true) {
				this.selector.select();// 會停在這等連線 or 訊息

				// work on selected keys
				Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
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
							this.accept(key);
						} catch (Exception e) {
							showMessage("有人斷線 - accept");
							key.channel().close();// 發現斷線則趕快中斷與他的連線 不然會報錯
						}
					} else if (key.isReadable()) { // 訊息key
						try {
							this.read(key);
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
			stopRequestTimer();
		}
		isStarted = true;
		return true;
	}

	@Override
	public boolean stopEngin() {
		showMessage("end");
		isStarted = false;
		return false;
	}

	private void startRequestTimer() {
		showMessage("開啟Request Timer...");
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				showMessage("開始發送Request...");
				for (String firewallID : STATION_SOCKET_MAP.keySet()) {
					SocketChannel channel = STATION_SOCKET_MAP.get(firewallID);
					boolean success = sendMessage(channel, String.format("$%sD", firewallID));
					if (!success) {
						showMessage("發送失敗  " + firewallID);
						try {
							channel.close();
							channel = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
						STATION_SOCKET_MAP.remove(firewallID);
					}
				}
				showMessage("Request發送結束");
			}
		};
		new Timer().schedule(timerTask, 5000, 1000 * 30);// TODO 發Request的時間間隔設定
		showMessage("Request Timer開啟完成");
	}

	private void stopRequestTimer() {
		showMessage("關閉Request Timer...");
		if (timerTask != null) {
			boolean success = timerTask.cancel();
			timerTask = null;
			if (success) {
				showMessage("Request Timer關閉完成");
			} else {
				showMessage("Request Timer關閉失敗");
			}
		}
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

		// 回傳ACK訊號
		try {
			boolean isData = react(channel, getMessage);
			if (isData) {
				List<OriginalWaterData<Double>> originalModelDatas = analysisEngin.analysisOriginalData(getMessage);
				WaterData waterData = new WaterData();
				if (originalModelDatas.size() != 0) {
					for (OriginalWaterData<Double> originalModelData : originalModelDatas) {
						waterData.setStid(originalModelData.getStid());
						waterData.setLasttime(originalModelData.getDatatime());
						Double[] datas = originalModelData.getDatas();
						waterData.setWaterlevel((datas[0] * 0.0001 - 1) * 8.75);// TODO 公式須依不同狀況修正 & 加入底床高
						waterData.setVoltage(datas[4] * 0.01);// TODO 公式須依不同狀況修正
						showMessage("取得資料 : " + waterData.getStid() + " " + waterData.getLasttime() + " " + waterData.getWaterlevel() + " " + waterData.getVoltage());

						WATER_DATA_QUEUE.add(waterData);
					}
				} else {
					showMessage("此筆判斷為資料,但無法解讀!");
				}

			}
		} catch (Exception e) {
			showMessage("處理回應訊息錯誤! 原始訊息:" + getMessage);
		}
		buffer.clear();
	}

	/**
	 * 針對傳來的訊息做相對的回應
	 * 
	 * @return boolean 是否為資料
	 */
	private boolean react(SocketChannel channel, String getMessage) {
		boolean isData = false;
		if (getMessage.length() == 10) {
			// 收到門號
			String fireWallID = STATION_WALL_ID_MAP.get(getMessage);
			if (fireWallID != null) {
				STATION_SOCKET_MAP.put(fireWallID, channel);
			} else {
				showMessage("無法識別的門號或訊息 : " + getMessage);
			}
		} else if (getMessage.charAt(0) == '#' && getMessage.split(",")[0].length() == 51) {
			// 收到資料
			String wallID = getMessage.substring(1, 4);
			String number = STATION_NUMBER_ID_MAP.get(wallID);
			String packageNumber = getMessage.substring(4, 9);
			if (number != null) {// 確認有此防火牆number再傳送訊息
				isData = true;
				sendMessage(channel, String.format("$%sK%s", wallID, packageNumber));
			} else {
				showMessage("無法辨識該防火牆ID的門號  : " + wallID);
			}
		} else if (getMessage.charAt(0) == '#' && getMessage.trim().length() == 26) {
			// 收到需要回傳ACK的要求 ex : #0050029816120113234100188
			try {
				String wallID = getMessage.substring(1, 4);
				String number = STATION_NUMBER_ID_MAP.get(wallID);
				String packageNumber = getMessage.substring(4, 9);
				@SuppressWarnings("unused")
				Date datatime = sdf.parse(getMessage.substring(9, 21));// 在這邊做日期判斷 若是格式正確 判定為正常訊息
				if (number != null) {// 確認有此防火牆number再傳送訊息
					isData = true;
					sendMessage(channel, String.format("$%sK%s", wallID, packageNumber));
				} else {
					showMessage("無法辨識該防火牆ID的門號  : " + wallID);
				}
			} catch (Exception e) {
				showMessage("無法識別的疑似要求ACK訊息 : " + getMessage);
			}
		} else if (getMessage.substring(4).trim().equals("00000240")) {
			showMessage("收到無資料訊號 - " + getMessage);
		} else {
			showMessage("無法識別的訊息 : " + getMessage);
		}
		return isData;
	}

	private boolean sendMessage(SocketChannel channel, String message) {
		boolean success = false;
		String outputMessage = message + "\r";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer = ByteBuffer.wrap(outputMessage.getBytes(Charset.forName("US-ASCII")));
		try {
			channel.write(buffer);
			showMessage("send message (" + channel.getRemoteAddress() + ") : " + outputMessage);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		buffer.clear();
		return success;
	}

	private void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
