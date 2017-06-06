package com.wavegis.engin.raw_data_analysis.kenkul;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyDatas;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.OriginalWaterData;
import com.wavegis.model.water.WaterData;

public class KenkulDataEngin extends TimerEngin {

	public static final String enginID = "KenkulDataAnalysis";
	private static final String enginName = "塏固資料分析2.0";
	private static final EnginView enginView = new KenkulDataEnginView();
	private static final OriginalDataAnalysisEngin originalDataAnalysisEngin = new OriginalDataAnalysisEngin(OriginalDataAnalysisEngin.TYPE_KenkulLORA);
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public KenkulDataEngin() {
		showMessage("分析Engin版本 : " + OriginalDataAnalysisEngin.enginID);
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
		String rawData;
		while ((rawData = ProxyDatas.KENKUL_RAW_DATA.poll()) != null) {
			List<OriginalWaterData<Double>> originalWaterDatas = originalDataAnalysisEngin.analysisOriginalData(rawData);
			for (OriginalWaterData<Double> originalWaterData : originalWaterDatas) {
				WaterData waterData = new WaterData();
				waterData.setLasttime(originalWaterData.getDatatime());
				waterData.setStid(originalWaterData.getStid());
				Double[] datas = originalWaterData.getDatas();
				try {
					if (datas[0] > 5000) {
						waterData.setWaterlevel((datas[0] - 5000) * 0.0001 * 625);
					} else {
						waterData.setWaterlevel(0);
					}
					waterData.setRainfallCounter(datas[2]);
					waterData.setVoltage(datas[4]);
					waterData.setRainfall10min(datas[6]);
					waterData.setRainfall1hour(datas[7]);
					waterData.setRainfall3hour(datas[8]);
					waterData.setRainfall6hour(datas[9]);
					waterData.setRainfall12hour(datas[10]);
					waterData.setRainfall24hour(datas[11]);

					showMessage("放入待INSERT Queue中 : " + waterData.getStid() + " " + simpleDateFormat.format(waterData.getLasttime()));
					ProxyDatas.WATER_DATA_INSERT_QUEUE.offer(waterData);
				} catch (Exception e) {
					showMessage("資料處理錯誤!!");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
