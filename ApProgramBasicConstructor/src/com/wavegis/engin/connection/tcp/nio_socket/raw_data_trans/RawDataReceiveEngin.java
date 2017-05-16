package com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

public class RawDataReceiveEngin implements Engin {

	public static final String enginID = "RawDataReceive";
	private static final String enginName = "原始資料接收";
	private static final RawDataReceiveEnginView enginView = new RawDataReceiveEnginView();
	private static Logger logger = LogTool.getLogger(RawDataReceiveEngin.class.getName());
	private boolean started = false;
	private boolean waitingForReturnMessage = false;
	private boolean restartingEngin = false;
	private SocketChannel socketChannel;

	private static final ConcurrentLinkedQueue<String> RAW_DATA_QUEUE = new ConcurrentLinkedQueue<>();//TODO

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
		return started;
	}

	@Override
	public boolean startEngin() {
		
		if (restartingEngin) {
			showMessage("重連程序執行中...請稍後");
			return false;
		}
		
		started = true;

		// #[[ 連線 & 註冊 & 開始等訊息
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					socketChannel = connect();
					regist(socketChannel);
					startListenDatas(socketChannel);
					startCheckConnectionTimer(socketChannel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// ]]

		return false;
	}

	private SocketChannel connect() throws IOException {
		// 依照設定的清單開啟連線
		String address = GlobalConfig.XML_CONFPIG.getProperty("RawDataReceiveTargetIP");
		int port = Integer.valueOf(GlobalConfig.XML_CONFPIG.getProperty("RawDataReceiveTargetPort"));
		InetSocketAddress hostAddress = new InetSocketAddress(address, port);
		try {
			SocketChannel socketChannel = SocketChannel.open(hostAddress);
			showMessage("連線成功.");
			return socketChannel;
		} catch (IOException e) {
			showMessage("Listener Error " + address + " " + port);
			e.printStackTrace();
			throw e;
		}
	}

	/** 向對方註冊自己ID 對方才會開始傳資料 */
	private void regist(SocketChannel clientChannel) {
		String registKey = GlobalConfig.XML_CONFPIG.getProperty("RawDataTransRegisteredKey");
		String registID = GlobalConfig.XML_CONFPIG.getProperty("RawDataReceiveID");
		String registString = registKey + "," + registID;
		ByteBuffer buffer = ByteBuffer.wrap(registString.getBytes());
		try {
			showMessage("註冊:" + registString + "  to :" + clientChannel.getRemoteAddress());
			clientChannel.write(buffer);
			buffer.clear();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void startListenDatas(final SocketChannel socketChannel) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showMessage("資料接收開始...");
				ByteBuffer buffer = ByteBuffer.allocate(4096 * 1024);
				while (started) {
					try {
						int numRead = -1;
						while ((numRead = socketChannel.read(buffer)) > 0) {
							byte[] data = new byte[numRead];
							System.arraycopy(buffer.array(), 0, data, 0, numRead);
							String originalData = new String(data);
							System.out.println(originalData);
							if (originalData.equals(GlobalConfig.XML_CONFPIG.getProperty("RawDataTransTestConnectionKey"))) {
								showMessage("取得測試連線回傳 : " + originalData);
							} else {
								showMessage("取得資料 : " + originalData);
								RAW_DATA_QUEUE.offer(originalData);
							}
							waitingForReturnMessage = false;
							buffer.clear();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				showMessage("資料接收結束.");
			}
		}).start();
	}

	private void startCheckConnectionTimer(final SocketChannel socketChannel) {
		showMessage("開始確認連線Timer...");
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (started) {
					if (!waitingForReturnMessage) {
						ByteBuffer buffer = ByteBuffer.wrap(GlobalConfig.XML_CONFPIG.getProperty("RawDataTransTestConnectionKey").getBytes());
						try {
							socketChannel.write(buffer);
						} catch (IOException e) {
							showMessage("連線測試錯誤 準備重連...");
							e.printStackTrace();
							startRestartEnginProcess();
						}
					} else {
						showMessage("過久無回傳資訊   準備重連...");
						startRestartEnginProcess();
					}
				} else {
					showMessage("連線Timer關閉.");
					this.cancel();
				}
			}
		};
		new Timer().schedule(timerTask, 1000, 1000 * 30);
	}

	private void startRestartEnginProcess() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showMessage("開始重連程序...");
				stopEngin();
				restartingEngin = true;
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				restartingEngin = false;
				startEngin();
			}
		}).start();
	}

	@Override
	public boolean stopEngin() {
		if (restartingEngin) {
			showMessage("重連程序執行中...請稍後");
			return false;
		}
		try {
			socketChannel.close();
			socketChannel = null;
		} catch (IOException e) {
			showMessage("SocketChannel關閉錯誤  " + e.getMessage());
			e.printStackTrace();
		}
		started = false;
		return true;
	}

	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
