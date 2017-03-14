package com.wavegis.engin.image.cctv.mjpeg;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class CCTVMutiThreadEnginView extends SimpleEnginView {

	public static final String enginID = CCTVMutiThreadEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
