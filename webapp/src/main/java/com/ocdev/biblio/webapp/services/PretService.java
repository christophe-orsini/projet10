package com.ocdev.biblio.webapp.services;

import java.security.Principal;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.ocdev.biblio.webapp.dto.ReservationDto;
import com.ocdev.biblio.webapp.objects.Pret;

public interface PretService
{
	public Page<Pret> listePrets(Principal abonne, int page, int taille) throws EntityNotFoundException;
	public Pret prolonger(Principal abonne, Long pretId) throws EntityNotFoundException;
	public Page<ReservationDto> listeReservations(Principal abonne, int page, int taille) throws EntityNotFoundException;
	public HttpStatus annulerReservation(Principal abonne, Long reservationId) throws EntityNotFoundException;
	public Pret reserver(Principal abonne, long ouvrageId) throws EntityNotFoundException;
	public HttpStatus retirerReservation(Principal abonne, Long reservationId) throws EntityNotFoundException;
}
