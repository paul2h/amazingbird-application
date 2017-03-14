package com.wavegis.engin.image.trans;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class ImageTransClientEnginView extends SimpleEnginView {

	private static final String enginID = ImageTransClientEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
