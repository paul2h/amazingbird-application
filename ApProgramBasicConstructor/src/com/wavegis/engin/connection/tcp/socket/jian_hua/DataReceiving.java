package com.wavegis.engin.connection.tcp.socket.jian_hua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.model.water.WaterData;

public class DataReceiving implements Runnable {
	private BufferedReader reader;
	private PrintStream sender;
	private SensorReceivingEngin engin;
	private Socket socket;

	public DataReceiving(SensorReceivingEngin engin, Socket socket) {
		this.engin = engin;
		this.socket = socket;

		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
			sender = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String ackMessage = GlobalConfig.XML_CONFIG.getProperty("SensorAckMessage", "ACK");
		String receiveingMessage;
		String ipAddress = socket.getRemoteSocketAddress().toString();

		try {
			engin.showMessage(ipAddress + " 連線開始");

			while (SensorReceivingEngin.isStarted) {
				receiveingMessage = reader.readLine();

				if (receiveingMessage == null) {
					break;
				}

				sender.println(ackMessage);// 有收到訊息就回ACK

				WaterData waterData = ConvertToWaterData.analysisNewData(receiveingMessage);

				if (waterData == null) {
					System.out.println("不處理的資料 : " + receiveingMessage);
					continue;
				}

				StringBuffer sb = new StringBuffer();

				sb.append(ipAddress + "'s original Message: " + receiveingMessage + "\n");

				if (waterData.getStid() == null) {
					continue;
				} else if (waterData.getLasttime() == null) {
					continue;
				}
				sb.append("stid: " + waterData.getStid() + ", lasttime: " + waterData.getLasttime() + "\n");

				if (waterData.getWaterlevel() >= 0) {
					sb.append("waterlevel: " + waterData.getWaterlevel() + ", voltage: " + waterData.getVoltage());
					ProxyData.WATER_DATA_INSERT_QUEUE.offer(waterData);
				}
				sb.append("\n");
				engin.showMessage(sb.toString());

			}
		} catch (IOException e) {
			engin.showMessage(ipAddress + " 連線失敗");
			e.printStackTrace();
		} catch (Exception e) {
			engin.showMessage(ipAddress + " 連線失敗");
			e.printStackTrace();
		}
	}
}
