package com.wavegis.engin.raw_data_analysis.kenkul;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.ProxyDatas;
import com.wavegis.global.tools.LogTool;
import com.wavegis.model.water.OriginalWaterData;

public class KenkulDataEngin extends TimerEngin {

	public static final String enginID = "KenkulDataAnalysis";
	private static final String enginName = "塏固資料分析";
	private static final EnginView enginView = new KenkulDataEnginView();
	private static final OriginalDataAnalysisEngin originalDataAnalysisEngin = new OriginalDataAnalysisEngin(OriginalDataAnalysisEngin.TYPE_KenkulLORA);
	private Logger logger = LogTool.getLogger(this.getClass().getName());

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
		showMessage("開始分析資料...");
		String rawData;
		List<OriginalWaterData<Double>> totalWaterDatas = new ArrayList<>();
		while ((rawData = ProxyDatas.KENKUL_RAW_DATA.poll()) != null) {
			List<OriginalWaterData<Double>> originalWaterDatas = originalDataAnalysisEngin.analysisOriginalData(rawData);
			totalWaterDatas.addAll(originalWaterDatas);
		}
		showMessage(String.format("共 %d 筆資料分析完成,放入待Insert Queue中", totalWaterDatas.size()));
		ProxyDatas.WATER_DATA_INSERT_QUEUE.addAll(totalWaterDatas);
	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
