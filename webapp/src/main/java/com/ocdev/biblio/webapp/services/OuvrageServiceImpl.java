package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ocdev.biblio.webapp.dto.OuvrageConsultDto;
import com.ocdev.biblio.webapp.dto.OuvrageCriteria;
import com.ocdev.biblio.webapp.dto.SearchOuvrageDto;
import com.ocdev.biblio.webapp.objects.Ouvrage;
import com.ocdev.biblio.webapp.utils.RestResponsePage;

@Service
public class OuvrageServiceImpl implements OuvrageService
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired RestTemplateService restTemplateService;
	@Autowired UtilisateurService utilisateurService;
	
	@Override
	public OuvrageConsultDto getOuvrageById(Principal abonne, long ouvrageId)
	{
		long abonneId = utilisateurService.getAbonneId(abonne);
	 	RestTemplate restTemplate = restTemplateService.buildRestTemplate();
	 	return restTemplate.getForObject(properties.getApiUrl() + "ouvrages/" +  ouvrageId + "/utilisateur/" + abonneId, OuvrageConsultDto.class);
	}

	@Override
	public RestResponsePage<Ouvrage> listeOuvrages(SearchOuvrageDto ouvrageCherche, int page, int taille)
	{
	 	RestTemplate restTemplate = restTemplateService.buildRestTemplate();
	 	
		ParameterizedTypeReference<RestResponsePage<Ouvrage>> responseType = 
				new ParameterizedTypeReference<RestResponsePage<Ouvrage>>() { };

		OuvrageCriteria ouvrageCriteria = new OuvrageCriteria(
				ouvrageCherche.getTitre(),
				ouvrageCherche.getAuteur(), 0,
				ouvrageCherche.getTheme(),
				ouvrageCherche.isDisponible() ? 1 : 0);
		
		HttpEntity<OuvrageCriteria> entity = new HttpEntity<OuvrageCriteria>(ouvrageCriteria);
		ResponseEntity<RestResponsePage<Ouvrage>> result = restTemplate.exchange(
				properties.getApiUrl() + "ouvrages/search", HttpMethod.POST, entity, responseType);
		
		return result.getBody();
	}
}
