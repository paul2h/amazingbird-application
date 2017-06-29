package com.wavegis.engin.image.cctv.fake_image;

import com.wavegis.engin.prototype.SimpleEnginView;

@SuppressWarnings("serial")
public class FakeImageEnginView extends SimpleEnginView{

	@Override
	public String getEnginID() {
		return FakeImageEngin.enginID;
	}

}
