package com.ocdev.biblio.webapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesConfigurationService
{
	@Value("${app.serveur.apiurl}") // injection via application.properties
	private String url="";

	public String getApiUrl()
	{
		if (url.endsWith("/")) return url;
		return url + "/";
	}
}
