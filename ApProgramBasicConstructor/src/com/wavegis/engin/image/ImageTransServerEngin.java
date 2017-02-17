package com.wavegis.engin.image;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class ImageTransServerEngin implements Engin {

	public static final String enginID = "ImageTransServer";
	private static final String enginName = "圖片傳輸ServerEngin";
	public static final ImageTransServerEnginView enginView = new ImageTransServerEnginView();
	private boolean started = false;
	private ConcurrentHashMap<SocketChannel, String> imageSettingMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, byte[]> imageTempMap = new ConcurrentHashMap<>();
	private static final String noPathMessage = "no path";

	private Thread runningThread;
	String[] fileNames = { "111.jpg", "222.jpg", "333.jpg", "444.jpg", "555.jpg", "666.jpg" };// TODO

	// #[[ nio socket 物件
	private ServerSocketChannel serverChannel;
	private InetSocketAddress listenAddress;
	private Selector selector;
	// ]]

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

		runningThread = new Thread(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				ServerSocket serverSocket = null;

				try {
					serverSocket = new ServerSocket(9999);// TODO
				} catch (IOException ex) {
					showMessage("Can't setup server on this port number. ");
				}

				Socket socket = null;
				InputStream in = null;
				OutputStream out = null;
				int fileCount = 0;
				while (true) {

					try {
						socket = serverSocket.accept();
					} catch (IOException ex) {
						showMessage("Can't accept client connection. ");
					}

					try {
						in = socket.getInputStream();
					} catch (IOException ex) {
						showMessage("Can't get socket input stream. ");
					}

					try {
						out = new FileOutputStream("D:\\" + fileNames[fileCount]);
					} catch (FileNotFoundException ex) {
						showMessage("File not found. ");
					}

					byte[] bytes = new byte[16 * 1024];

					int count;
					try {
						while ((count = in.read(bytes)) > 0) {
							out.write(bytes, 0, count);
						}

						out.close();

						fileCount++;
						if (fileCount >= fileNames.length) {
							fileCount = 0;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// out.close();
				// in.close();
				// socket.close();
				// serverSocket.close();
			}
		});

		runningThread.start();
		started = true;
		return true;
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
		imageSettingMap.put(channel, noPathMessage);
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	// read from the socket channel
	private void read(SelectionKey key) throws IOException {
		ArrayList<Byte> fullImageData = new ArrayList<Byte>();
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(40395);
		if (imageSettingMap.get(channel).equals(noPathMessage)) {// 還沒設定路徑
			int numRead = -1;
			numRead = channel.read(buffer);
			if (numRead == -1) {// 正常斷線key
				this.imageSettingMap.remove(channel);
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
			if (getMessage.split(",")[0].equals("#name#")) {
				imageSettingMap.put(channel, "D://temp//" + getMessage.split(",")[1] + ".jpg");
			} else {
				showMessage("無法辨識訊息 :" + getMessage);
			}
			buffer.clear();

		} else {// 已設定路徑 接收圖片

			while (channel.read(buffer) > 0) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					fullImageData.add(buffer.get());
				}
				buffer.clear();
			}

			Object[] data;
			data = fullImageData.toArray();
			byte[] bytes = new byte[data.length];
			for (int i = 0; i < data.length; i++) {
				bytes[i] = Byte.valueOf(data[i].toString());
			}
			showMessage("Got image byte length : " + data.length);
			imageTempMap.put(imageSettingMap.get(channel), bytes);

			InputStream is = new ByteArrayInputStream(bytes);
			BufferedImage image = ImageIO.read(is);
			showMessage("Got image : " + image);
			ImageIO.write(image, "jpg", new File(imageSettingMap.get(channel)));
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean stopEngin() {

		try {
			showMessage("stop Engin...");
			runningThread.stop();
			serverChannel.close();
			serverChannel = null;
			selector.close();
			selector = null;
			started = false;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	private void showMessage(String message) {
		enginView.showMessage(message);
	}

}
