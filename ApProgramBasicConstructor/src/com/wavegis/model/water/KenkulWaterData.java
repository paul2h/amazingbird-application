package com.wavegis.model.water;

import java.sql.Timestamp;

public class KenkulWaterData implements OriginalWaterData<Double> {

	private String stid;
	private Timestamp datatime;
	private Double[] datas;
	private String originalDataString;

	public String getStid() {
		return stid;
	}

	public void setStid(String stid) {
		this.stid = stid;
	}

	public Timestamp getDatatime() {
		return datatime;
	}

	public void setDatatime(Timestamp datatime) {
		this.datatime = datatime;
	}

	public Double[] getDatas() {
		return datas;
	}

	public void setDatas(Double[] datas) {
		this.datas = datas;
	}

	public String getOriginalDataString() {
		return originalDataString;
	}

	public void setOriginalDataString(String originalDataString) {
		this.originalDataString = originalDataString;
	}

}
