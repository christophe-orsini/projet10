package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ocdev.biblio.webapp.objects.Pret;
import com.ocdev.biblio.webapp.objects.Utilisateur;
import com.ocdev.biblio.webapp.utils.RestResponsePage;

@Service
public class PretServiceImpl implements PretService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	
	@Override
	public Page<Pret> listePrets(Principal abonne, int page, int taille) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = getAbonneId(abonne);
	 	
		ParameterizedTypeReference<RestResponsePage<Pret>> responseType = 
				new ParameterizedTypeReference<RestResponsePage<Pret>>() { };
		
		ResponseEntity<RestResponsePage<Pret>> result = restTemplate.exchange(
				properties.getApiUrl() + "prets/" + abonneId, HttpMethod.GET, null, responseType);
		
		return result.getBody();
	}

	@Override
	public Pret prolonger(Principal abonne, Long pretId)
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = getAbonneId(abonne);
		
		ResponseEntity<Pret> result = restTemplate.exchange(
				properties.getApiUrl() + "prets/prolonge/" + pretId + "/utilisateur/" + abonneId,
				HttpMethod.PUT, null, Pret.class);
		
		return result.getBody();
	}

	private Long getAbonneId(Principal abonne)
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		// recherche de l'abonné
		Utilisateur utilisateur;
		try
		{
			ResponseEntity<Utilisateur> response = restTemplate.getForEntity(
					properties.getApiUrl() + "utilisateurs/" + abonne.getName(), Utilisateur.class);
			utilisateur = response.getBody();
		}
		catch (HttpClientErrorException e)
		{
			throw new EntityNotFoundException("L'abonné n'existe pas");
		}
		
		return utilisateur.getId();
	}
}
