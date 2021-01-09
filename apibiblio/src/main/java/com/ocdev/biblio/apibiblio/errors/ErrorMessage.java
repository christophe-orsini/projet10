package com.ocdev.biblio.apibiblio.errors;

public class ErrorMessage
{
	private int statusCode;
	private String reason;
	private String message;
	
	public ErrorMessage(int statusCode, String reason, String message)
	{
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.message = message;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public String getReason()
	{
		return reason;
	}

	public String getMessage()
	{
		return message;
	}
}
