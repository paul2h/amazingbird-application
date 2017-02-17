package com.wavegis.engin.image;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class ImageTransServerEnginView extends SimpleEnginView{
	
	private static final String enginID = ImageTransServerEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
