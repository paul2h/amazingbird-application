package com.wavegis.engin.image.http_image_typhoon;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class TyphoonImageEnginView extends SimpleEnginView{
	
	private static final String enginID = TyphoonImageEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
