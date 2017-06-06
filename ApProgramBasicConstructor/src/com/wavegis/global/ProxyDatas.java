package com.wavegis.global;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.water.WaterData;

public class ProxyDatas {
	public static ConcurrentLinkedQueue<String> KENKUL_RAW_DATA = new ConcurrentLinkedQueue<String>();
	public static ConcurrentLinkedQueue<WaterData> WATER_DATA_INSERT_QUEUE =new ConcurrentLinkedQueue<>();
}
