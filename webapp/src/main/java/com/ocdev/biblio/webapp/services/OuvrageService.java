package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import org.springframework.data.domain.Page;

import com.ocdev.biblio.webapp.dto.OuvrageConsultDto;
import com.ocdev.biblio.webapp.dto.SearchOuvrageDto;
import com.ocdev.biblio.webapp.objects.Ouvrage;

public interface OuvrageService
{
	public Page<Ouvrage> listeOuvrages(SearchOuvrageDto ouvrageCherche, int page, int taille);
	public OuvrageConsultDto getOuvrageById(Principal abonne, long ouvrageId);
}
