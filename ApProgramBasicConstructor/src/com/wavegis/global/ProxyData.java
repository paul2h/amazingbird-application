package com.wavegis.global;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.wavegis.model.WaterData;

public class ProxyData {

	public static ConcurrentLinkedQueue<WaterData> WATER_INSERT_QUEUE = new ConcurrentLinkedQueue<>();
	
}
