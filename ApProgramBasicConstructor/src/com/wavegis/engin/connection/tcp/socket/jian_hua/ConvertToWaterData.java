package com.wavegis.engin.connection.tcp.socket.jian_hua;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.wavegis.global.ProxyData;
import com.wavegis.model.water.WaterData;

public class ConvertToWaterData {
	private static HashMap<String, Double> lastTimeRainData = new HashMap<String, Double>();
	private static ConcurrentHashMap<String, ConcurrentSkipListMap<String, Double>> accumulatedRainfallDatas = new ConcurrentHashMap<String, ConcurrentSkipListMap<String, Double>>();
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 進來的資料格式:<br>
	 * 
	 * @,001,20151027173940,2, JH-A3G AI1 5.2, JH-A3G DI1 1,%<br>
	 *                         封包編號 日期 兩個資料 編號 水位 雨量<br>
	 *                         AI1代表水位<br>
	 *                         DI1代表雨量
	 */
	public static synchronized WaterData analysisNewData(String message) throws Exception {
		WaterData waterData = new WaterData();

		try {
			String[] analysisMessage = message.split(",");

			if (analysisMessage.length < 4) {
				return null;
			}
			// 時間
			String dataTime = analysisMessage[2].trim();

			waterData.setLasttime(stringToTimestamp(dataTime));
			// 資料數
			int dataCount = Integer.valueOf(analysisMessage[3].trim());
			// 從第4個開始是純data 取得純data的陣列
			String[] pureDatas = (String[]) Arrays.copyOfRange(analysisMessage, 4, 4 + dataCount);

			// 根據封包的資料數來擷取資料(此時每個陣列的內容應該類似: JH-A3G AI1 5.2 共30位元(每個資訊10位元))
			for (int i = 0; i < dataCount; i++) {
				// 取得編號並去除空格
				String stid = pureDatas[i].substring(0, 10).trim();
				// 取得資料種類並去除空格(AI1或DI1)
				String type = pureDatas[i].substring(10, 20).trim().substring(0, 3);
				// 取得數據
				double data = Double.valueOf(pureDatas[i].substring(20, 30).trim());

				if (waterData.getStid() == null) {
					waterData.setStid(stid);
				}
				// 判斷資料種類
				switch (type) {
				case "AI1":
					// 水位傳來的單位是cm 轉成m
					data = data / 100;

					if (ProxyData.RIVER_BOTTOM_DATAS.containsKey(stid)) {
						data = data + ProxyData.RIVER_BOTTOM_DATAS.get(stid).getBottom_height();
					}
					waterData.setWaterlevel(data);

					break;
				case "DI1":
					double rainfall = 0;

					ConcurrentSkipListMap<String, Double> accumulatedRainfallData = accumulatedRainfallDatas.get(stid);
					if (accumulatedRainfallData == null) {
						accumulatedRainfallData = new ConcurrentSkipListMap<String, Double>();
					}
					String finalTime = formatDatetime(dataTime);

					if (accumulatedRainfallData.containsKey(finalTime)) {
						return null;//判斷已經有同樣資料過  回傳一個無意義物件
					}
					if (lastTimeRainData.containsKey(stid)) {// 如果該雨量站前面已經有收到資料(此筆資料不是第一筆資料)
						// 建華的雨量累積方式較不同 by kiwi
						double lastRainData = lastTimeRainData.get(stid);

						if (data - lastRainData >= 0) {// 新的資料比舊資料大或一樣 -> 降雨量為 "新資料減舊資料"
							rainfall = data - lastRainData;
						}
					}
					waterData.setRainfallCounter(rainfall);
					lastTimeRainData.put(stid, data);
					accumulatedRainfallData.put(finalTime, rainfall);
					// 計算累積雨量
					String tenMinutesAgo = formatDatetime(getAddTime(dataTime, Calendar.MINUTE, -10));
					String anHourAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -1));
					String threeHourAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -3));
					String sixHourAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -6));
					String halfOfDayAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -12));
					String eighteenHoursAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -18));
					String oneDayAgo = formatDatetime(getAddTime(dataTime, Calendar.DATE, -1));
					String oneAndAHalfDaysAgo = formatDatetime(getAddTime(dataTime, Calendar.HOUR_OF_DAY, -36));
					String threeDaysAgo = formatDatetime(getAddTime(dataTime, Calendar.DATE, -3));
					double rainfall_10min = getAccumulatedRainfall(accumulatedRainfallData, tenMinutesAgo, finalTime, 0, true); // 累積雨量(10分鐘)
					double rainfall_hour = getAccumulatedRainfall(accumulatedRainfallData, anHourAgo, tenMinutesAgo, rainfall_10min, false); // 累積雨量(1小時)
					double rainfall_3hr = getAccumulatedRainfall(accumulatedRainfallData, threeHourAgo, anHourAgo, rainfall_hour, false); // 累積雨量(3小時)
					double rainfall_6hr = getAccumulatedRainfall(accumulatedRainfallData, sixHourAgo, threeHourAgo, rainfall_3hr, false); // 累積雨量(6小時)
					double rainfall_12hr = getAccumulatedRainfall(accumulatedRainfallData, halfOfDayAgo, sixHourAgo, rainfall_6hr, false); // 累積雨量(12小時)
					double rainfall_18hr = getAccumulatedRainfall(accumulatedRainfallData, eighteenHoursAgo, halfOfDayAgo, rainfall_12hr, false); // 累積雨量(18小時)
					double rainfall_24hr = getAccumulatedRainfall(accumulatedRainfallData, oneDayAgo, eighteenHoursAgo, rainfall_18hr, false); // 累積雨量(24小時)
					double rainfall_36hr = getAccumulatedRainfall(accumulatedRainfallData, oneAndAHalfDaysAgo, oneDayAgo, rainfall_24hr, false); // 累積雨量(48小時)
					double rainfall_72hr = getAccumulatedRainfall(accumulatedRainfallData, threeDaysAgo, oneAndAHalfDaysAgo, rainfall_36hr, false); // 累積雨量(72小時)

					waterData.setRainfall10min(rainfall_10min);
					waterData.setRainfall1hour(rainfall_hour);
					waterData.setRainfall3hour(rainfall_3hr);
					waterData.setRainfall6hour(rainfall_6hr);
					waterData.setRainfall12hour(rainfall_12hr);
					waterData.setRainfall24hour(rainfall_24hr);
					waterData.setRainfall36hour(rainfall_36hr);
					waterData.setRainfall72hour(rainfall_72hr);

					accumulatedRainfallData = abandonSurplusData(accumulatedRainfallData, threeDaysAgo);
					accumulatedRainfallDatas.put(stid, accumulatedRainfallData);

					break;
				case "AI4":// 電壓
					waterData.setVoltage(data);
					waterData.setVoltage(data);

					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return waterData;
	}

	/** 特別處理時間資料為 "yyyyMMddHHmmss" */
	private static Timestamp stringToTimestamp(String date) {
		Timestamp outputTime = null;
		try {
			outputTime = new Timestamp(formatter.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outputTime;
	}

	/**
	 * 回傳從傳入時間推移指定時間量的時間
	 * 
	 * @param date
	 * @param field
	 *            指定時間量詞
	 * @param amount
	 *            指定時間
	 * @return
	 */
	private static Date getAddTime(String datetime, int field, int amount) {
		Date date = new Date();

		try {
			date = formatter.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getAddTime(date, field, amount);
	}

	private static Date getAddTime(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	private static String formatDatetime(String datetime) {
		Date date = new Date();
		try {
			date = formatter.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDatetime(date);
	}

	private static String formatDatetime(Date date) {
		return new SimpleDateFormat("yyyyMMddHHmm").format(date);
	}

	/**
	 * 取得累積雨量
	 * 
	 * @param accumulatedRainfallData
	 * @param start
	 *            起始時間
	 * @param end
	 *            結束時間
	 * @param accumulatedRainfall
	 *            已累積雨量
	 * @param isCalSatart
	 *            是否計算起始時間的雨量
	 * @return
	 */
	private static double getAccumulatedRainfall(ConcurrentSkipListMap<String, Double> accumulatedRainfallData, String start, String end, double accumulatedRainfall, boolean isCalSatart) {
		String firstKey = start;
		String lastKey = end;
		ConcurrentNavigableMap<String, Double> subAccumulatedRainfallData = null;

		if (accumulatedRainfallData == null || accumulatedRainfallData.size() == 0) {
			return accumulatedRainfall;
		}
		subAccumulatedRainfallData = (ConcurrentNavigableMap<String, Double>) accumulatedRainfallData.subMap(firstKey, isCalSatart, lastKey, true);
		if (subAccumulatedRainfallData == null || subAccumulatedRainfallData.isEmpty()) {
			return accumulatedRainfall;
		}
		for (String mapKey : subAccumulatedRainfallData.keySet()) {
			double rainfall = subAccumulatedRainfallData.get(mapKey);
			accumulatedRainfall += rainfall;
		}

		return accumulatedRainfall;
	}

	private static ConcurrentSkipListMap<String, Double> abandonSurplusData(ConcurrentSkipListMap<String, Double> accumulatedRainfallData, String datatime) {
		String key = accumulatedRainfallData.lowerKey(datatime);
		if (key == null) {
			return accumulatedRainfallData;
		}
		ConcurrentNavigableMap<String, Double> subRainfallDatas = accumulatedRainfallData.tailMap(key);

		accumulatedRainfallData = null;
		accumulatedRainfallData = new ConcurrentSkipListMap<String, Double>(subRainfallDatas);

		return accumulatedRainfallData;
	}
}
