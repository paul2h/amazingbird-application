package com.wavegis.engin.db.insert.flood;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.FileTool;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.TextTool;
import com.wavegis.model.flood.FloodLogData;

public class FloodKMLInsertEngin extends TimerEngin {

	public final static String enginID = "FloodKMLInsert";
	private final static String enginName = "淹水KML資料寫入1.0";
	private final static EnginView enginView = new FloodKMLInsertEnginView();
	private Logger logger = LogTool.getLogger(this.getClass().getName());
	private FloodKMLInsertDao dao = null;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
	private String floodKMLDirPath = GlobalConfig.XML_CONFIG.getProperty("FloodKMLDirPath");
	private String floodKMLBackupDirPath = GlobalConfig.XML_CONFIG.getProperty("FloodKMLBackupDirPath");

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
		if (dao == null) {
			showMessage("DAO初始化...");
			dao = FloodKMLInsertDao.getInstance();
			showMessage("DAO初始化完成.");
		}


		File[] KMLFiles = FileTool.scanFold(floodKMLDirPath);
		List<FloodLogData> logDatas = new ArrayList<>();
		for (File KMLFile : KMLFiles) {
			if (!KMLFile.isDirectory() && KMLFile.getName().length() > 20) {
				String kmlFilePath = KMLFile.getAbsolutePath();
				String originalText = readOriginalText(kmlFilePath);
				showMessage("開始解析文字..." + kmlFilePath);
				try {
					String filtText = getTargetText(originalText, "<coordinates>", "</coordinates>");// TODO
					String polygonString;

					polygonString = getPolygonString(filtText);

					String stid = KMLFile.getName().substring(11, 20);
					String dateString = KMLFile.getName().substring(0, 10);
					FloodLogData logData = new FloodLogData();
					logData.setStid(stid);
					logData.setDatatime(simpleDateFormat.parse(dateString));
					logData.setPolygon_string(polygonString);
					logDatas.add(logData);

					showMessage("解析文字完成." + kmlFilePath);

					showMessage("搬移檔案 : " + kmlFilePath);
					FileTool.moveFile(kmlFilePath, floodKMLBackupDirPath + KMLFile.getName());
				} catch (IOException e) {
					e.printStackTrace();
					showMessage("解析文字錯誤 : " + e.getMessage());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if (logDatas.size() > 0) {
			showMessage("開始寫入資料...");
			dao.insertFloodData(logDatas);
			showMessage("資料寫入完成,筆數 : " + logDatas.size());
		}
	}

	private String readOriginalText(String filePath) {
		try {
			return TextTool.readText(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private String getTargetText(String originalText, String TargetStartText, String TargetEndText) {
		String filtText = null;
		if (originalText != null) {
			filtText = TextTool.getTargetText(originalText, TargetStartText, TargetEndText);
		}
		return filtText;
	}

	/** 針對KML文件做處理 取得polygon字串 */
	private String getPolygonString(String polygonText) throws IOException {
		boolean isFirstPoint = true;
		String polygonString = "POLYGON((";// TODO
		String[] polygonPointTexts = polygonText.split(",0\n");
		for (int i = 0; i < polygonPointTexts.length; i++) {
			String polygonPointText = polygonPointTexts[i];
			if (!isFirstPoint && polygonPointText.split(",").length > 1) {
				polygonString += ",";
			}
			if (polygonPointText.split(",").length > 1) {
				polygonString += polygonPointText.replaceAll("\n", "").replace(",", " ");
				isFirstPoint = false;
			}
			if (i == polygonPointTexts.length - 1) {
				polygonString += "))";
			}
		}

		return polygonString;
	}

	@Override
	public void showMessage(String message) {
		logger.info(message);
		enginView.showMessage(message);
	}

}
