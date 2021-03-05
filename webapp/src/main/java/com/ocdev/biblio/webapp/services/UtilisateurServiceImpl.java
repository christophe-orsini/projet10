package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.ocdev.biblio.webapp.dto.UtilisateurDto;
import com.ocdev.biblio.webapp.errors.BiblioException;
import com.ocdev.biblio.webapp.objects.Utilisateur;

@Service
public class UtilisateurServiceImpl implements UtilisateurService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	
	@Override
	public Utilisateur inscription(UtilisateurDto utilisateurDto) throws BiblioException
	{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Utilisateur> response;
		
		try
		{
			 response = restTemplate.postForEntity(
					properties.getApiUrl() + "utilisateurs", utilisateurDto, Utilisateur.class);
		}
		catch (Exception e)
		{
			throw new BiblioException("Erreur : L'utilisateur existe déjà");
		}
		
		if (response == null || response.getStatusCode() != HttpStatus.CREATED)
		{
			throw new BiblioException("Erreur : L'utilisateur n'est pas créé");
		}

		return response.getBody();
	}

	@Override
	public long getAbonneId(Principal abonne)
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
