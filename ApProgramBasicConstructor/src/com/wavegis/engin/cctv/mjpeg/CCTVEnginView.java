package com.wavegis.engin.cctv.mjpeg;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CCTVEnginView extends SimpleEnginView{

	private static final String enginID = "CCTV";
	@Override
	public String getEnginID() {
		return enginID;
	}

}
