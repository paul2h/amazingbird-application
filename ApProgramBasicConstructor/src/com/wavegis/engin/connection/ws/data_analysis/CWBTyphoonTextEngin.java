package com.wavegis.engin.connection.ws.data_analysis;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.wavegis.engin.prototype.EnginView;
import com.wavegis.engin.prototype.TimerEngin;
import com.wavegis.global.tools.LogTool;
import com.wavegis.global.tools.TextTool;

/**
 * 專門針對颱風文字檔(.cap)解讀並產出html
 * 
 * @author Wavegis
 *
 */
public class CWBTyphoonTextEngin extends TimerEngin {

	public static final String enginID = "CWBTyphoonText";
	private static final String enginName = "氣象局颱風文字分析Engin";
	private static final CWBTyphoonTextEnginView enginView = new CWBTyphoonTextEnginView();
	private static final Logger logger = LogTool.getLogger(CWBTyphoonTextEngin.class.getName());

	private static final String DescriptionStartTag = "<description>";
	private static final String DescriptionEndTag = "</description>";
	private static final String DescriptionReplaceTag = "<description_here>";
	
	String capTargetDir = "D://"; // TODO
	String htmlTemplateFilePath = "D://cwb_typhoon_template.html"; // TODO
	String htmlTargetFilePath = "D://test.html"; // TODO

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

		try {
			String originalText = null;
			String fileName = "W-C0034-001.cap";//TODO
			// #[[ 過濾分析文字
			String description_text = null;
			originalText = TextTool.readText(capTargetDir + fileName);
			if (originalText != null) {
				showMessage("開始解析文字...");
				int startIndex = originalText.indexOf(DescriptionStartTag) + DescriptionStartTag.length();
				int endIndex = originalText.indexOf(DescriptionEndTag);
				description_text = originalText.substring(startIndex, endIndex);
				description_text = description_text.replaceAll("\\[", "</dd><dt>[");
				description_text = description_text.replaceAll("\\]", "]</dt>\n<dd>");
				description_text = description_text + "</dd>";
				description_text = description_text.substring(6, description_text.length());
				showMessage("解析文字完成.");
			}
			// ]]

			// #[[ 放入樣板中,輸出html檔案
			if (description_text != null) {
				showMessage("開始套用樣板...");
				String templatText = TextTool.readText(htmlTemplateFilePath);
				templatText = templatText.replaceAll(DescriptionReplaceTag, description_text);
				showMessage("開始產出html...");
				boolean success = TextTool.writeText(htmlTargetFilePath, templatText);
				if (success) {
					showMessage("產出完成.");
				} else {
					showMessage("產出失敗.");
				}
			}
			// ]]
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void showMessage(String message) {
		enginView.showMessage(message);
		logger.info(message);
	}

}
