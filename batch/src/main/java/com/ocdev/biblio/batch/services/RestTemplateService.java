package com.ocdev.biblio.batch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateBuilder restTemplateBuilder;
	
	public RestTemplate buildRestTemplate() throws AuthenticationException
	{
	 	return restTemplateBuilder.basicAuthentication(properties.login(), properties.password()).build();
	}
}
