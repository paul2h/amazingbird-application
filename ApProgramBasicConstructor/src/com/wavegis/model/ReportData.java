package com.wavegis.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReportData implements Comparable<ReportData> {

	public static final String OK = "OK";
	public static final String WEB_ERROR = "WEB_ERROR";
	public static final String DB_ERROR = "DB_ERROR";
	public static final String MISS_SIGNAL = "MISS_SIGNAL";
	@SuppressWarnings("serial")
	public static final ConcurrentHashMap<String, String> ChineseStatusMap = new ConcurrentHashMap<String, String>() {
		{
			put(OK, "正常");
			put(WEB_ERROR, "網頁連線異常");
			put(DB_ERROR, "資料接收異常");
			put(MISS_SIGNAL, "監控子程式斷訊");
		}
	};

	private String projectName;
	private String status;
	private Object data;
	private Timestamp reportTime;

	public ReportData() {
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Timestamp getReportTime() {
		return reportTime;
	}

	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}

	@Override
	public String toString() {
		return "ReportData [projectName=" + projectName + ", status=" + status + ", data=" + data + ", reportTime=" + reportTime + "]";
	}

	public String getMailContent() {
		StringBuilder mailContent = new StringBuilder();
		mailContent.append("專案名稱: " + projectName + "<br>");
		mailContent.append("錯誤類型: " + ChineseStatusMap.get(status) + "<br>");
		System.out.println("start instanceof ");
		if (data instanceof List<?>) {
			for (Object obj : (List<?>) data) {
				mailContent.append(obj + "<br>");
			}
		}
		mailContent.append("reportTime: " + reportTime + "<br>");
		return mailContent.toString();
	}

	@Override
	public int compareTo(ReportData o) {
		return 0;
	}

}
