package com.wavegis.global;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.WaterData;

public class ProxyData {
	public static ConcurrentLinkedQueue<WaterData> WATER_INSERT_QUEUE = new ConcurrentLinkedQueue<WaterData>();
	public static ConcurrentHashMap<Object, Object> ENGIN_PRELOAD_MAP = new ConcurrentHashMap<Object, Object>();
}
