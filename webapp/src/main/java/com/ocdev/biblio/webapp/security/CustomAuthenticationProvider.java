package com.ocdev.biblio.webapp.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ocdev.biblio.webapp.objects.Role;
import com.ocdev.biblio.webapp.services.PropertiesConfigurationService;
import com.ocdev.biblio.webapp.services.RestTemplateService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
    	String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        if (!authentication.isAuthenticated())
        {
        	RestTemplate restTemplate = restTemplateService.buildRestTemplate(login, password);
            
            ResponseEntity<String> userResponse = null;
        	try
    		{
        		userResponse = restTemplate.postForEntity(properties.getApiUrl() + "checklogin", null, String.class);
    		}
    		catch (RestClientException e)
    		{
    			throw new BadCredentialsException("External system authentication failed");
    		}
    		if (userResponse == null || userResponse.getStatusCode() != HttpStatus.OK) return null;
        }
        
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(Role.ROLE_ABONNE.toString()));
		
        return new UsernamePasswordAuthenticationToken(login, password, authorities); 
    }
 
    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
