package com.wavegis.engin.image.http_image;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class HttpImageGetEnginView extends SimpleEnginView{
	
	private static final String enginID = HttpImageGetEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
