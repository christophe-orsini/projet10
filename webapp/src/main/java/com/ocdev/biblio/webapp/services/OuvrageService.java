package com.ocdev.biblio.webapp.services;

import org.springframework.data.domain.Page;

import com.ocdev.biblio.webapp.dto.SearchOuvrageDto;
import com.ocdev.biblio.webapp.objects.Ouvrage;

public interface OuvrageService
{
	public Page<Ouvrage> listeOuvrages(SearchOuvrageDto ouvrageCherche, int page, int taille);
	public Ouvrage getOuvrageById(Long id);
}
