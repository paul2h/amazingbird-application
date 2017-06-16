package com.wavegis.model.water;

import java.sql.Timestamp;

/**
 * 原始水情資料初步解析
 * 
 * @author Wavegis
 *
 */
public interface OriginalWaterData<T> {

	public void setStid(String stid);

	public String getStid();

	public void setDatatime(Timestamp datatime);

	public Timestamp getDatatime();

	public void setDatas(T[] datas);

	public T[] getDatas();

	public void setOriginalDataString(String originalDataString);

	public String getOriginalDataString();

}
