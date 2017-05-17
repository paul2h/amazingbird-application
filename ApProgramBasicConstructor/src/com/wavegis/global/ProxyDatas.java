package com.wavegis.global;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.water.OriginalWaterData;

public class ProxyDatas {
	public static ConcurrentLinkedQueue<String> KENKUL_RAW_DATA = new ConcurrentLinkedQueue<String>();
	public static ConcurrentLinkedQueue<OriginalWaterData<Double>> WATER_DATA_INSERT_QUEUE = new ConcurrentLinkedQueue<>();
}
