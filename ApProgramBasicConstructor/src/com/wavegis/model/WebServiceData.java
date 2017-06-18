package com.wavegis.model;

/**
 * WebService相關資訊
 * 
 * @author kiwi
 *
 */
public class WebServiceData {

	private String webServiceID;
	private String webServiceName;
	private String URL;
	private String WSDL_URL;

	public String getWebServiceID() {
		return webServiceID;
	}

	public void setWebServiceID(String webServiceID) {
		this.webServiceID = webServiceID;
	}

	public String getWebServiceName() {
		return webServiceName;
	}

	public void setWebServiceName(String webServiceName) {
		this.webServiceName = webServiceName;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getWSDL_URL() {
		return WSDL_URL;
	}

	public void setWSDL_URL(String wSDL_URL) {
		WSDL_URL = wSDL_URL;
	}

}
