package com.wavegis.model.esav;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AuthAction implements Serializable
{
	private String Username;
	private String Message;
	private String EncryptMessage;
	private String TimeStamp;
	private String Format;
	
	public AuthAction(String userName , String message , String encryptMessage , String timeStamp , String format) {
		Username = userName;
		Message = message;
		EncryptMessage = encryptMessage;
		Format = format;
		TimeStamp = timeStamp;
	}

	public final String getUsername()

	{
		return Username;
	}

	public final void setUsername(String value)
	{
		Username = value;
	}

	public final String getMessage()
	{
		return Message;
	}

	public final void setMessage(String value)
	{
		Message = value;
	}

	public final String getEncryptMessage()
	{
		return EncryptMessage;
	}

	public final void setEncryptMessage(String value)
	{
		EncryptMessage = value;
	}

	public final String getTimeStamp()
	{
		return TimeStamp;
	}

	public final void setTimeStamp(String value)
	{
		TimeStamp = value;
	}

	public final String getFormat()
	{
		return Format;
	}

	public final void setFormat(String value)
	{
		Format = value;
	}
}