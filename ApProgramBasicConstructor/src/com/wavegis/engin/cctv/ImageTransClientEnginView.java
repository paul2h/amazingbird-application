package com.wavegis.engin.cctv;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class ImageTransClientEnginView extends SimpleEnginView {

	private static final String enginID = ImageTransClientEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
