package com.ocdev.biblio.webapp.services;

import java.security.Principal;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ocdev.biblio.webapp.dto.ReservationDto;
import com.ocdev.biblio.webapp.objects.Pret;
import com.ocdev.biblio.webapp.utils.RestResponsePage;

@Service
public class PretServiceImpl implements PretService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	@Autowired UtilisateurService utilisateurService;
	
	@Override
	public Page<Pret> listePrets(Principal abonne, int page, int taille) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = utilisateurService.getAbonneId(abonne);
	 	
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
		Long abonneId = utilisateurService.getAbonneId(abonne);
		
		ResponseEntity<Pret> result = restTemplate.exchange(
				properties.getApiUrl() + "prets/prolonge/" + pretId + "/utilisateur/" + abonneId,
				HttpMethod.PUT, null, Pret.class);
		
		return result.getBody();
	}
	
	public Page<ReservationDto> listeReservations(Principal abonne, int page, int taille) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = utilisateurService.getAbonneId(abonne);
	 	
		ParameterizedTypeReference<RestResponsePage<ReservationDto>> responseType = 
				new ParameterizedTypeReference<RestResponsePage<ReservationDto>>() { };
		
		ResponseEntity<RestResponsePage<ReservationDto>> result = restTemplate.exchange(
				properties.getApiUrl() + "reservations/" + abonneId, HttpMethod.GET, null, responseType);
		
		return result.getBody();
	}

	@Override
	public HttpStatus annulerReservation(Principal abonne, Long reservationId) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = utilisateurService.getAbonneId(abonne);
		
		ResponseEntity<Pret> result = restTemplate.exchange(
				properties.getApiUrl() + "reservations/annuler/" + reservationId + "/utilisateur/" + abonneId,
				HttpMethod.PUT, null, Pret.class);
		
		return result.getStatusCode();
	}
	
	@Override
	public HttpStatus retirerReservation(Principal abonne, Long reservationId) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = utilisateurService.getAbonneId(abonne);
		
		ResponseEntity<Pret> result = restTemplate.exchange(
				properties.getApiUrl() + "reservations/retirer/" + reservationId + "/utilisateur/" + abonneId,
				HttpMethod.PUT, null, Pret.class);
		
		return result.getStatusCode();
	}

	@Override
	public Pret reserver(Principal abonne, long ouvrageId) throws EntityNotFoundException
	{
		RestTemplate restTemplate = restTemplateService.buildRestTemplate();
		
		// recherche de l'abonné
		Long abonneId = utilisateurService.getAbonneId(abonne);
		
		ResponseEntity<Pret> result = restTemplate.exchange(
				properties.getApiUrl() + "/reservations/abonne/" + abonneId + "/ouvrage/" + ouvrageId,
				HttpMethod.PUT, null, Pret.class);
		
		return result.getBody();
	}
	
	
}
