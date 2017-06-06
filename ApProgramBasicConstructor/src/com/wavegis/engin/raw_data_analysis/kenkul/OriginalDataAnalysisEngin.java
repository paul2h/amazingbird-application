package com.wavegis.engin.raw_data_analysis.kenkul;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.wavegis.engin.prototype.AnalysisEngin;
import com.wavegis.model.water.KenkulWaterData;
import com.wavegis.model.water.OriginalWaterData;

public class OriginalDataAnalysisEngin implements AnalysisEngin<OriginalWaterData<Double>> {

	public static final String enginID = "AnalysisEngin1.0";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
	private static HashMap<String, Double> lastTimeRainData = new HashMap<String, Double>();
	private static ConcurrentHashMap<String, ConcurrentSkipListMap<String, Double>> accumulatedRainfallDatas = new ConcurrentHashMap<String, ConcurrentSkipListMap<String, Double>>();
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

	// #[[ 塏固用參數
	public static final int TYPE_Kenkul = 0;
	public static final int TYPE_KenkulLORA = 1;
	private static final String KENKUL_STID_HEAD = "KEN"; // 塏固的STID固定加上開頭以利分辨
	private static final String KENKUL_LORA_HEAD = "KL-"; // 塏固的LORA測站STID固定加上開頭以利分辨
	// ]]

	private int analysisType = TYPE_Kenkul;

	public OriginalDataAnalysisEngin() {
	}

	public OriginalDataAnalysisEngin(int type) {
		analysisType = type;
	}

	/**
	 * 原始資料分析(皆為最原始資料,無計算)
	 * 
	 * <pre>
	 * 塏固WSN資料: 
	 * 	data[0] = 第一組資料 , 
	 * 	data[1] = 第二組資料 , 
	 * 	data[2] = 雨量計數資料 , 
	 * 	data[3] = 溫度 ,
	 * 	data[4] = WSN模組電池電壓
	 * 塏固LORA資料:
	 * 	data[0] = 第一組資料 , 
	 * 	data[1] = 第二組資料 , 
	 * 	data[2] = 雨量計數資料 , 
	 * 	data[3] = 溫度 ,
	 * 	data[4] = 電池電壓
	 * 	data[5] = RSSI信號強度
	 *	data[6] = 十分鐘雨量
	 *	data[7] = 時雨量
	 *	data[8] = 3小時雨量
	 *	data[9] = 6小時雨量
	 *	data[10] = 12小時雨量
	 *	data[11] = 24小時雨量
	 * </pre>
	 */
	@Override
	public synchronized List<OriginalWaterData<Double>> analysisOriginalData(String originalMessage) {
		List<OriginalWaterData<Double>> result = new ArrayList<>();
		if (analysisType == TYPE_Kenkul) {
			// WSN格式資料
			// #00300096 161130 175932 01 003+23712-00225 00000 40347 343,098
			try {
				OriginalWaterData<Double> waterData = new KenkulWaterData();
				waterData.setOriginalDataString(originalMessage);
				String stid = KENKUL_STID_HEAD + originalMessage.substring(1, 4);
				waterData.setStid(stid);
				String dateString = originalMessage.substring(9, 21);
				Timestamp datatime = new Timestamp(sdf.parse(dateString).getTime());
				waterData.setDatatime(datatime);

				double data01 = Double.valueOf(originalMessage.substring(27, 32));
				double data02 = Double.valueOf(originalMessage.substring(33, 38));
				double rainCount = Double.valueOf(originalMessage.substring(38, 43));
				double temperature = Double.valueOf(originalMessage.substring(43, 48));
				String voltageString = originalMessage.substring(48, 51);
				double voltage = Double.valueOf(voltageString);
				Double[] datas = { data01, data02, rainCount, temperature, voltage };
				waterData.setDatas(datas);

				result.add(waterData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (analysisType == TYPE_KenkulLORA) {
			// LORA格式資料
			// #0020958517030407554804002AD2-01249+071760000003782327-033,001AD2-01249+071760000003782327-033,003AD2-01249+071760000003782327-033,004AD2-01249+071760000003782327-033,253
			// #00209585 170304 075548 04 002 AD2 -01249 +07176 00000 0 378 2 327 -033 ,001AD2-01249+071760000003782327-033,003AD2-01249+071760000003782327-033,004AD2-01249+071760000003782327-033,253
			try {

				String stidHead = KENKUL_LORA_HEAD + originalMessage.substring(1, 4);
				String dateString = originalMessage.substring(9, 21);
				Timestamp datatime = new Timestamp(sdf.parse(dateString).getTime());

				// 資料讀取
				int dataCount = Integer.valueOf(originalMessage.substring(21, 23));
				String[] dataStrings = originalMessage.substring(23, originalMessage.lastIndexOf(",")).split(",");
				if (dataStrings.length == dataCount) {
					for (int i = 0; i < dataCount; i++) {

						// #[[ 基本解讀
						OriginalWaterData<Double> waterData = new KenkulWaterData();
						String dataString = dataStrings[i];
						String stid = stidHead + dataString.subSequence(0, 3);
						waterData.setStid(stid);
						waterData.setDatatime(datatime);
						waterData.setOriginalDataString(originalMessage);
						double data01 = Double.valueOf(dataString.substring(7, 12));
						double data02 = Double.valueOf(dataString.substring(13, 18));
						double rainCount = Double.valueOf(dataString.substring(18, 23));
						double temperature = Double.valueOf(dataString.substring(23, 28));
						double voltage = Double.valueOf(dataString.substring(28, 31)) * 0.01;
						double RSSISignal = Double.valueOf(dataString.substring(32, 35));

						// ]]

						// #[[ 計算各累積雨量
						double rainfall = 0;

						ConcurrentSkipListMap<String, Double> accumulatedRainfallData = accumulatedRainfallDatas.get(stid);
						if (accumulatedRainfallData == null) {
							accumulatedRainfallData = new ConcurrentSkipListMap<String, Double>();
						}
						String finalTime = dateString;

						if (accumulatedRainfallData.containsKey(finalTime)) {
							System.out.println("已有此筆資料,跳過不處理 : " + stid + "  " + finalTime);
							continue;
						}
						if (lastTimeRainData.containsKey(stid)) {// 如果該雨量站前面已經有收到資料(此筆資料不是第一筆資料)

							double lastRainData = lastTimeRainData.get(stid);
							if (rainCount - lastRainData >= 0) {// 新的資料比舊資料大或一樣 -> 降雨量為 "新資料減舊資料"
								rainfall = rainCount - lastRainData;
							}
						}
						lastTimeRainData.put(stid, rainCount);
						accumulatedRainfallData.put(finalTime, rainfall);
						accumulatedRainfallDatas.put(stid, accumulatedRainfallData);
						// 計算累積雨量
						String tenMinutesAgo = formatDatetime(getAddTime(dateString, Calendar.MINUTE, -10));
						String anHourAgo = formatDatetime(getAddTime(dateString, Calendar.HOUR_OF_DAY, -1));
						String threeHourAgo = formatDatetime(getAddTime(dateString, Calendar.HOUR_OF_DAY, -3));
						String sixHourAgo = formatDatetime(getAddTime(dateString, Calendar.HOUR_OF_DAY, -6));
						String halfOfDayAgo = formatDatetime(getAddTime(dateString, Calendar.HOUR_OF_DAY, -12));
						String oneDayAgo = formatDatetime(getAddTime(dateString, Calendar.DATE, -1));
						double rainfall_10min = getAccumulatedRainfall(accumulatedRainfallData, tenMinutesAgo, finalTime, 0, true); // 累積雨量(10分鐘)
						double rainfall_hour = getAccumulatedRainfall(accumulatedRainfallData, anHourAgo, finalTime, 0, true); // 累積雨量(1小時)
						double rainfall_3hr = getAccumulatedRainfall(accumulatedRainfallData, threeHourAgo, finalTime, 0, true); // 累積雨量(3小時)
						double rainfall_6hr = getAccumulatedRainfall(accumulatedRainfallData, sixHourAgo, finalTime, 0, true); // 累積雨量(6小時)
						double rainfall_12hr = getAccumulatedRainfall(accumulatedRainfallData, halfOfDayAgo, finalTime, 0, true); // 累積雨量(12小時)
						double rainfall_24hr = getAccumulatedRainfall(accumulatedRainfallData, oneDayAgo, finalTime, 0, true); // 累積雨量(24小時)
						// ]]
						// #[[ 放入清單
						Double[] datas = { data01, data02, rainCount, temperature, voltage, RSSISignal,
								rainfall_10min, rainfall_hour, rainfall_3hr, rainfall_6hr, rainfall_12hr, rainfall_24hr };
						waterData.setDatas(datas);
						result.add(waterData);
						// ]]
					}
				} else {
					System.out.println("資料宣稱長度與實際長度不符!!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
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
	private double getAccumulatedRainfall(ConcurrentSkipListMap<String, Double> accumulatedRainfallData, String start, String end, double accumulatedRainfall, boolean isCalSatart) {
		ConcurrentNavigableMap<String, Double> subAccumulatedRainfallData = null;

		if (accumulatedRainfallData == null || accumulatedRainfallData.size() == 0) {
			return accumulatedRainfall;
		}

		subAccumulatedRainfallData = (ConcurrentNavigableMap<String, Double>) accumulatedRainfallData.tailMap(start, isCalSatart).headMap(end, true);

		if (subAccumulatedRainfallData == null || subAccumulatedRainfallData.isEmpty()) {
			return accumulatedRainfall;
		}
		for (String mapKey : subAccumulatedRainfallData.keySet()) {
			double rainfall = subAccumulatedRainfallData.get(mapKey);
			accumulatedRainfall += rainfall;
		}
		return accumulatedRainfall;
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
	private Date getAddTime(String datetime, int field, int amount) {
		Date date = new Date();

		try {
			date = formatter.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getAddTime(date, field, amount);
	}

	private Date getAddTime(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	private String formatDatetime(Date date) {
		return formatter.format(date);
	}

	/** 設定分析後的資料種類 */
	public void setAnalysisType(int type) {
		analysisType = type;
	}

	public int getAnalysisType() {
		return analysisType;
	}

}
