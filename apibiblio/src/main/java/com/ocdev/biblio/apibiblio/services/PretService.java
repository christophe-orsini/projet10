package com.ocdev.biblio.apibiblio.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.AvailableException;
import com.ocdev.biblio.apibiblio.errors.DelayLoanException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.FullWaitingQueueException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;
import com.ocdev.biblio.apibiblio.errors.NotEnoughCopiesException;

/**
 * Interface de déclaration des services pour les prets
 * @author C.Orsini
 */
public interface PretService
{
	/**
	 * Création d'un prêt pour un ouvrage et un abonné.
	 * @param abonneId L'ID de l'abonné
	 * @param ouvrageId L'ID de l'ouvrage
	 * @return Le prêt créé
	 * @param requesterName Le login(email) du demandeur
	 * @throws AlreadyExistsException levée si un prêt en cours existe déjà pour cet ouvrage et cet abonné
	 * @throws EntityNotFoundException levée si l'ouvrage ou l'abonné n'existent pas
	 * @throws NotEnoughCopiesException levée s'il n'y a pas assez d'exemplaires pour preter l'ouvrage
	 * @throws NotAllowedException levée si la demande ne vient pas de l'emprunteur ou d'un employé
	 */
	public Pret creer(Long abonneId, Long ouvrageId, String requesterName) throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, NotAllowedException;
	/**
	 * Retour d'un prêt.
	 * @param pretId L'ID du prêt
	 * @param utilisateurId L'ID du demandeur
	 * @param requesterName Le login(email) du demandeur
	 * @throws EntityNotFoundException levée si le prêt n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de l'emprunteur ou d'un employé
	 */
	public void retournerOuvrage(Long pretId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException;
	/**
	 * Permet de prolonger un prêt
	 * @param pretId L'ID du prêt
	 * @param utilisateurId L'ID de l'utilisateur demandeur de la prolongation
	 * @param requesterName Le login(email) du demandeur
	 * @return Le prêt modifié
	 * @throws EntityNotFoundException levée si le prêt n'existe pas
	 * @throws DelayLoanException levée si le prêt ne peut plus être prolongé
	 * @throws NotAllowedException levée si la demande ne vient pas de l'emprunteur ou d'un employé
	 */
	public Pret prolonger(Long pretId, Long utilisateurId, String requesterName) throws EntityNotFoundException, DelayLoanException, NotAllowedException;
	/**
	 * Retoune la liste des prêts d'un abonné
	 * @param abonneId L'ID de l'abonné
	 * @param paging Pagination
	 * @param requesterName Le login(email) du demandeur
	 * @return La liste des prêt qui peut être vide
	 * @throws EntityNotFoundException levée si l'abonné n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de l'emprunteur ou d'un employé
	 */
	public Page<Pret> listerSesPrets(Long abonneId, Pageable paging, String requesterName) throws EntityNotFoundException, NotAllowedException;
	/**
	 * Permet de consulter un prêt
	 * @param pretId L'ID du prêt
	 * @return Le prêt
	 * @throws EntityNotFoundException levée si le prêt n'existe pas
	 */
	public Pret consulter(Long pretId) throws EntityNotFoundException;
	/**
	 * Retourne la liste des prêts en retard.
	 * Compare la date en argument avec la date de fin prévu
	 * @param dateMaxi La date maximale de comparaison
	 * @return La liste des prêts avec une date de fin prévue supérieure à la date en argument
	 */
	public Collection<Pret> pretsEnRetard(Date dateMaxi);
	/**
	 * Réservation d'un ouvrage non disponible
	 * @param abonneId L'ID de l'abonné
	 * @param ouvrageId L'ID de l'ouvrage
	 * @param requesterName Le login(email) du demandeur
	 * @return Le prêt avec le statut "Reservé"
	 * @throws AlreadyExistsException levée si un prêt en cours (ou une réservation) existe déjà pour cet ouvrage et cet abonné
	 * @throws EntityNotFoundException levée si l'ouvrage ou l'abonné n'existent pas
	 * @throws NotEnoughCopiesException levée s'il n'y a pas d'exemplaires pour cet ouvrage
	 * @throws FullWaitingQueueException levée s'il la file de demande de réservation pour cet ouvrage est pleine
	 * @throws NotAllowedException levée si l'ouvrage est disponible
	 * @throws AvailableException 
	 */
	public Pret reserver(Long abonneId, Long ouvrageId, String requesterName) 
			throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, FullWaitingQueueException, NotAllowedException, AvailableException;
	/**
	 * Annulation d'une réservation.
	 * @param reservationId L'ID de la réservation
	 * @param utilisateurId L'ID du demandeur
	 * @param requesterName Le login(email) du demandeur
	 * @throws EntityNotFoundException levée si la réservation n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de la personne qui a réservé ou d'un employé
	 */
	public void annulerReservation(Long reservationId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException;
	/**
	 * Retoune la liste des réservations d'un abonné
	 * @param abonneId L'ID de l'abonné
	 * @param requesterName Le login(email) du demandeur
	 * @return La liste des réservations qui peut être vide
	 * @throws EntityNotFoundException levée si l'abonné n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de la personne qui a réservé ou d'un employé
	 */
	public Collection<ReservationDto> listerReservationsAbonne(Long abonneId, String requesterName) throws EntityNotFoundException, NotAllowedException;
	/**
	 * Retrait d'un ouvrage réservé et disponible suite à envoi d'email.
	 * @param reservationId L'ID de la réservation
	 * @param utilisateurId L'ID du demandeur
	 * @param requesterName Le login(email) du demandeur
	 * @return Le prêt créé
	 * @throws EntityNotFoundException levée si la réservation n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de la personne qui a réservé ou d'un employé
	 */
	public Pret retirerReservation(Long reservationId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException;
	/**
	 * Retourne la liste des réservations disponibles.
	 * @return La liste des réservations disponibles
	 */
	public Collection<Pret> reservationsDisponibles();
	/**
	 * Met à true le champs emailEnvoye et sauvegarde pour une liste de reservations
	 * @param emailsEnvoyés
	 */
	void setEmailsEnvoyes(Collection<Long> reservationIDs);
	/**
	 * Annule une liste de réservations échues
	 * @param reservationsEchues
	 * @throws EntityNotFoundException
	 * @throws NotAllowedException levée si la demande ne vient pas de la personne qui a réservé ou d'un employé
	 */
	void annulerReservations(Collection<Long> reservationIDs);
}
