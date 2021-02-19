package com.ocdev.biblio.batch.services;

import java.util.Collection;
import java.util.Date;
import com.ocdev.biblio.batch.model.Pret;
import com.ocdev.biblio.batch.model.Utilisateur;

public interface PretService
{
	/**
	 * Retourne une liste de prêts non retourné à une date
	 * @param date Date en dessous de laquelle on considère que le pret est à lister
	 * @return La liste des prets
	 */
	public Collection<Pret> listePretsEnCoursADate(Date date);
	/**
	 * Liste les prets par abonné
	 * 
	 * Converti une liste de pêts en une liste d'abonnées avec leurs prêts
	 * @param prets La liste des prets
	 * @return La liste des abonnés avec leurs prets respectifs
	 */
	public Collection<Utilisateur> pretsParAbonne(Collection<Pret> prets);
	/**
	 * Retourne une liste de réservations disponibles
	 * @return La liste des réservations
	 */
	public Collection<Pret> listeReservationsDisponibles();
}
