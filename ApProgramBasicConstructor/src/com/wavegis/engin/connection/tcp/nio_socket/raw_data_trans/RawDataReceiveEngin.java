package com.wavegis.engin.connection.tcp.nio_socket.raw_data_trans;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyDatas;
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
		String address = GlobalConfig.XML_CONFIG.getProperty("RawDataReceiveTargetIP");
		int port = Integer.valueOf(GlobalConfig.XML_CONFIG.getProperty("RawDataReceiveTargetPort"));
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
		String registKey = GlobalConfig.XML_CONFIG.getProperty("RawDataTransRegisteredKey");
		String registID = GlobalConfig.XML_CONFIG.getProperty("RawDataReceiveID");
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
				while (started) {
					String originalData;
					try {
						originalData = listenMessage(socketChannel);
						checkHeaderProcess(originalData);
					} catch (IOException e) {
						e.printStackTrace();
						showMessage("連線 or 資料判斷錯誤  進入重連程序");
						startRestartEnginProcess();
					}
				}
				showMessage("資料接收結束.");
			}
		}).start();
	}

	private String listenMessage(SocketChannel socketChannel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4096 * 1024);
		String stringMessage = null;
		int numRead = -1;
		while ((numRead = socketChannel.read(buffer)) > 0) {
			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);
			stringMessage = new String(data);
			waitingForReturnMessage = false;
			buffer.clear();
		}
		return stringMessage;
	}

	private void checkHeaderProcess(String originalData) {
		if (originalData.equals(GlobalConfig.XML_CONFIG.getProperty("RawDataTransTestConnectionKey").toString())) {
			showMessage("取得測試連線回傳 : " + originalData);
		} else if (originalData.indexOf(GlobalConfig.XML_CONFIG.getProperty("KenkulRawDataHeader").toString()) >= 0) {
			showMessage("取得塏固資料 : " + originalData);
			ProxyDatas.KENKUL_RAW_DATA.offer(originalData.replaceAll(GlobalConfig.XML_CONFIG.getProperty("KenkulRawDataHeader"), ""));
		} else {
			showMessage("取得無法辨識資料 : " + originalData);
		}
	}

	private void startCheckConnectionTimer(final SocketChannel socketChannel) {
		showMessage("開始確認連線Timer...");
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (started) {
					if (!waitingForReturnMessage) {
						ByteBuffer buffer = ByteBuffer.wrap(GlobalConfig.XML_CONFIG.getProperty("RawDataTransTestConnectionKey").getBytes());
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
					showMessage("60秒後開始重連...");
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
