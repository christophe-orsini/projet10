package com.ocdev.biblio.apibiblio.utils;

import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

@Component
public class AppSettings
{
	
	public static int getIntSetting(String setting)
	{
		String result = getSetting(setting);

		return Integer.parseInt(result);
	}
	
	
	public static String getSetting(String setting)
	{
		ResourceBundle prop = ResourceBundle.getBundle("config");
		
		String result =  prop.getString("config." + setting);
		
		return result;
	}
}
