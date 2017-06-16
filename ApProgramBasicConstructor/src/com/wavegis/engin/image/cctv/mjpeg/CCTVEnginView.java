package com.wavegis.engin.image.cctv.mjpeg;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CCTVEnginView extends SimpleEnginView{

	private static final String enginID = CCTVEngin.enginID;
	@Override
	public String getEnginID() {
		return enginID;
	}

}
