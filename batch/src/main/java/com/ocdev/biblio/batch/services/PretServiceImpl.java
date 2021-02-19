package com.ocdev.biblio.batch.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ocdev.biblio.batch.model.Pret;
import com.ocdev.biblio.batch.model.Utilisateur;

@Service
public class PretServiceImpl implements PretService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	
	@Override
	public Collection<Pret> listePretsEnCoursADate(Date date)
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		ParameterizedTypeReference<Collection<Pret>> responseType = 
				new ParameterizedTypeReference<Collection<Pret>>() { };
				
		ResponseEntity<Collection<Pret>> result = restTemplate.exchange(
				properties.apiUrl() + "prets/retard?dateMaxi=" + format.format(date), 
				HttpMethod.GET, null, responseType);
		
		if (result.getStatusCode() != HttpStatus.OK)
		{
			return new ArrayList<Pret>();
			
		}
		
		return result.getBody();
	}
	
	@Override
	public Collection<Utilisateur> pretsParAbonne(Collection<Pret> prets)
	{
		if (prets == null || prets.isEmpty())
		{
			return new ArrayList<Utilisateur>();
		}
		
		Map<String, Utilisateur> utilisateurs = new HashMap<String, Utilisateur>();
		for (Pret pret : prets)
		{
			if (utilisateurs.containsKey(pret.getAbonne().getNom()))
			{
				// update
				utilisateurs.get(pret.getAbonne().getNom()).getPrets().add(pret);
			}
			else
			{
				// add
				Utilisateur utilisateur = new Utilisateur(pret.getAbonne().getEmail(), null,
						pret.getAbonne().getNom(), 
						pret.getAbonne().getPrenom());
				utilisateur.setPrets(new ArrayList<Pret>());
				utilisateur.getPrets().add(pret);
				utilisateurs.put(pret.getAbonne().getNom(), utilisateur);
			}
		}
		
		return utilisateurs.values();
	}

	@Override
	public Collection<Pret> listeReservationsDisponibles()
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
			
		ParameterizedTypeReference<Collection<Pret>> responseType = 
				new ParameterizedTypeReference<Collection<Pret>>() { };
				
		ResponseEntity<Collection<Pret>> result = restTemplate.exchange(
				properties.apiUrl() + "reservations/disponible", 
				HttpMethod.GET, null, responseType);
		
		if (result.getStatusCode() != HttpStatus.OK)
		{
			return new ArrayList<Pret>();
		}
		
		return result.getBody();
	}	
}
