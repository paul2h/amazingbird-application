package com.wavegis.engin.image.trans;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.wavegis.engin.prototype.Engin;
import com.wavegis.engin.prototype.EnginView;

public class ImageTransServerEngin implements Engin {

	public static final String enginID = "ImageTransServer";
	private static final String enginName = "圖片傳輸ServerEngin";
	public static final ImageTransServerEnginView enginView = new ImageTransServerEnginView();
	private boolean started = false;

	private Thread runningThread;
	String[] fileNames = { "111.jpg", "222.jpg", "333.jpg", "444.jpg", "555.jpg", "666.jpg" };// TODO

	// #[[ nio socket 物件
	private ServerSocketChannel serverChannel;
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
