package com.wavegis.engin.cctv;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class FakeImageEnginView extends SimpleEnginView{

	@Override
	public String getEnginID() {
		return FakeImageEngin.enginID;
	}

}
