package com.wavegis.engin.connection.ftp;

import com.wavegis.engin.SimpleEnginView;

@SuppressWarnings("serial")
public class FTPFileTransEnginView extends SimpleEnginView{

	private static final String enginID = FTPFileTransEngin.enginID;

	@Override
	public String getEnginID() {
		return enginID;
	}

}
