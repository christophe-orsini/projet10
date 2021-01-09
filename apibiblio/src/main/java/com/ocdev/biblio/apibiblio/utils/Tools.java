package com.ocdev.biblio.apibiblio.utils;

public class Tools
{
	public static boolean stringIsNullOrEmpty(String value)
	{
		if (value == null || value.isEmpty()) return true;
		
		return value.trim().isEmpty();
	}
}
