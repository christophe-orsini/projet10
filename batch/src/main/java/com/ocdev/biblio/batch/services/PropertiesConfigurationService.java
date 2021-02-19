package com.ocdev.biblio.batch.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesConfigurationService
{
	// injection via application.properties
	
	@Value("${app.serveur.apiurl}") 
	private String _url="";
	public String apiUrl()
	{
		if (_url.endsWith("/")) return _url;
		return _url + "/";
	}
	
	@Value("${app.serveur.login}")
	private String _login="";
	public String login()
	{
		return _login;
	}
	
	@Value("${app.serveur.password}")
	private String _password="";
	public String password()
	{
		return _password;
	}
	
	@Value("${app.param.societe}") 
	private String _societe="";
	public String societe()
	{
		return _societe;
	}
	
	@Value("${app.param.emailcontact}") 
	private String _emailContact="";
	public String emailContact()
	{
		return _emailContact;
	}
	
	@Value("${app.param.urlwebapp}") 
	private String _urlWebApp="";
	public String urlWebApp()
	{
		return _urlWebApp;
	}
	
	@Value("${app.param.emailsubject}") 
	private String _emailSubject="";
	public String emailSubject()
	{
		return _emailSubject;
	}
	
	@Value("${app.param.reservation.emailsubject}") 
	private String _emailReservationSubject="";
	public String emailReservationSubject()
	{
		return _emailReservationSubject;
	}
	
	@Value("${spring.mail.host}")
	private String _host="";
	public String host()
	{
		return _host;
	}
	
	@Value("${spring.mail.port}")
	private int _port;
	public int port()
	{
		return _port;
	}
	
	@Value("${spring.mail.protocol}")
	private String _protocol="";
	public String protocol()
	{
		return _protocol;
	}
	
	@Value("${spring.mail.username}")
	private String _username="";
	public String username()
	{
		return _username;
	}
	
	@Value("${spring.mail.password}")
	private String _emailPassword="";
	public String emailPassword()
	{
		return _emailPassword;
	}
	
	@Value("${spring.mail.default-encoding}")
	private String _encoding="";
	public String encoding()
	{
		return _encoding;
	}
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean _needAuth;
	public boolean needAuth()
	{
		return _needAuth;
	}
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean _startTls;
	public boolean startTls()
	{
		return _startTls;
	}
}
