package com.wavegis.engin.cctv;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class ImageTransServerEnginView extends SimpleEnginView{
	
	private static final String enginID = ImageTransServerEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
