package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;

import com.ocdev.biblio.webapp.objects.Pret;

public interface PretService
{
	public Page<Pret> listePrets(Principal abonne, int page, int taille) throws EntityNotFoundException;
	public Pret prolonger(Principal abonne, Long pretId) throws EntityNotFoundException;
}
