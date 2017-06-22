package com.wavegis.engin.ws.maoli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axis.encoding.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.wavegis.engin.connection.ws.others.maoli.MaoliWebSeriveEnginView;
import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.ProxyData;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.WaterData;
import com.wavegis.model.esav.AuthAction;
import com.wavegis.model.esav.AuthInfo;
import com.wavegis.model.esav.DataByID;
import com.wavegis.model.esav.KeyResult;
import com.wavegis.model.esav.ReadDataByPQidParam;
import com.wavegis.model.esav.ReadDataByStidParam;
import com.wavegis.model.esav.StationDataInfo;
import com.wavegis.model.esav.StationInfo;

public class MaoliWebSeriveEngin extends TimerEngin {
	private static final String enginID = "MaoliWebSerive";
	private static final String enginName = "苗栗WebService";
	private static final MaoliWebSeriveEnginView enginView = new MaoliWebSeriveEnginView();

	private Logger logger = LogTool.getLogger(this.getClass().getName());

	public MaoliWebSeriveEngin() {
		setTimeout(GlobalConfig.CONFPIG_PROPERTIES.getProperty("WS_Time_Period"));
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
	public void timerAction() {
		showMessage("開始取得資料...");
		AuthInfo authInfo = new AuthInfo();
		HashMap<String, Object> existMap = new HashMap<String, Object>();
		StationInfo[] stationInfos = null;
		String actionURL = GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServiceActionURL");

		authInfo.setUsername(GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServiceUser"));
		authInfo.setPassword(GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServicePassword"));

		existMap.put("水位", "");
		existMap.put("10分鐘累積雨量", "");

		KeyResult keyResult = (KeyResult) getResponseData(
				GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServiceURL") + "GetKey", authInfo,
				KeyResult.class);

		showMessage("取得測站資料中...");
		if (stationInfos == null) {
			String actionName = "ReadSTInfo";

			stationInfos = (StationInfo[]) getResponseData(actionURL + actionName,
					createAuthAction(keyResult, actionName), StationInfo[].class);
		}
		showMessage("取得各別測站的測量資料中...");

		// List<RainStationData> rainStationDatas = new
		// ArrayList<RainStationData>();
		// List<RawStationData> rawStationDatas = new
		// ArrayList<RawStationData>();
		String stActionName = "ReadPQInfoBySTId";
		String pqActionName = "ReadRealTimeDataByPQId";
		AuthAction action = createAuthAction(keyResult, stActionName);
		AuthAction rawAction = createAuthAction(keyResult, pqActionName);
		ReadDataByStidParam stParam = new ReadDataByStidParam();
		ReadDataByPQidParam pqParam = new ReadDataByPQidParam();

		stParam.setUserAction(action);
		pqParam.setUserAction(rawAction);

		for (StationInfo stationInfo : stationInfos) {
			try {
				String stid = stationInfo.getSTId();
				String stname = stationInfo.getName();
				WaterData waterData = new WaterData();

				stParam.setsTId(stid);
				waterData.setStid(stid);
				waterData.setStname(stname);

				showMessage("測站: " + stid + " " + stname);

				StationDataInfo[] stationDatas = (StationDataInfo[]) getResponseData(actionURL + stActionName, stParam,
						StationDataInfo[].class);

				for (StationDataInfo dataInfo : stationDatas) {
					String dataName = dataInfo.getName();
					String dataPQID = dataInfo.getPQId();

					if (!dataName.trim().isEmpty()) {
						showMessage("資料類型: " + dataName + ", PQID: " + dataPQID);

						if (!existMap.containsKey(dataName)) {
							continue;
						}
						pqParam.setpQId(dataPQID);

						DataByID data = (DataByID) getResponseData(actionURL + pqActionName, pqParam, DataByID.class);

						if (data != null) {
							if ("水位".equals(dataName)) {
								/*
								 * RawStationData rawData = new
								 * RawStationData();
								 * 
								 * rawData.setDatatime(Timestamp.valueOf(data.
								 * getData().getTimeStampIso()));
								 * rawData.setName(stName);
								 * rawData.setStno(stID);
								 * rawData.setPQID(data.getPQId());
								 * rawData.setWaterlevel(data.getData().getValue
								 * ());
								 * 
								 * rawStationDatas.add(rawData);
								 */
								waterData.setLasttime(Timestamp.valueOf(data.getData().getTimeStampIso()));
								waterData.setWaterlevel(data.getData().getValue());

								showMessage("水位 : " + waterData.getWaterlevel());
							}
							if ("10分鐘累積雨量".equals(dataName)) {
								/*
								 * RainStationData rainData = new
								 * RainStationData();
								 * 
								 * rainData.setDatatime(Timestamp.valueOf(data.
								 * getData().getTimeStampIso()));
								 * rainData.setName(stname);
								 * rainData.setStid(stid);
								 * rainData.setPQID(data.getPQId());
								 * rainData.setRain(data.getData().getValue());
								 * 
								 * rainStationDatas.add(rainData);
								 */
								waterData.setRainfall10min(data.getData().getValue());

								showMessage("雨量: " + waterData.getRainfall10min());
							}
						}
					}
				}
				ProxyData.WATER_INSERT_QUEUE.offer(waterData);
			} catch (Exception e) {
				e.printStackTrace();
				showMessage("資料取得有誤!!  " + e.getMessage());
			}
		}
		showMessage("資料取得完畢.");

	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

	private AuthAction createAuthAction(KeyResult keyResult, String actionName) {
		String encryption = dataEncryption(keyResult, actionName);

		return new AuthAction(GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServiceUser"), actionName,
				encryption, DateFormat.getInstance().format(new Date()), "json");
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private Object getResponseData(String url_suffix, KeyResult keyResult, String actionName, Class classOfT) {
		String encryption = dataEncryption(keyResult, actionName);
		AuthAction action = new AuthAction(GlobalConfig.CONFPIG_PROPERTIES.getProperty("MaoliWebServiceUser"),
				actionName, encryption, DateFormat.getInstance().format(new Date()), "json");

		return getResponseData(url_suffix, action, classOfT);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getResponseData(String url, Object object, Class classOfT) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
		HttpResponse response = null;
		StringBuffer sb = new StringBuffer();

		try {
			request.setHeader("Content-Type", "application/json; charset=utf-8");
			request.setEntity(new StringEntity(new Gson().toJson(object)));

			response = httpClient.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				showMessage("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String output = "";

			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Gson().fromJson(sb.toString(), classOfT);
	}

	private String dataEncryption(KeyResult keyResult, String message) {
		String computeResult = "";
		String encryptKey = keyResult.getKey(); // 加密的KEY
		String encryptText = message; // 要加密的文字
		String MAC_NAME = "HmacSHA1";
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), MAC_NAME);// 輸入參數為資料byte
																					// &
																					// 指定一個密鑰算法的名稱
		try {
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(MAC_NAME);
			// 用给定密钥初始化 Mac 对象
			mac.init(secretKey);
			// 完成 Mac 操作
			computeResult = Base64.encode(mac.doFinal(encryptText.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return computeResult;
	}
}
