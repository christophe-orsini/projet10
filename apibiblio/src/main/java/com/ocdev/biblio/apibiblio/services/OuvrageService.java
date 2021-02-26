package com.ocdev.biblio.apibiblio.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ocdev.biblio.apibiblio.criterias.OuvrageCriteria;
import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;

/**
 * Interface de déclaration des services pour les ouvrages.
 * @author C.Orsini
 */
public interface OuvrageService
{
	/**
	 * Méthode permettant d'ajouter.
	 * 
	 * @param ouvrageCreateDto Le DTO de l'ouvrage à créer
	 * @return L'ouvrage créé
	 * @throws AlreadyExistsException levée si le titre existe déjà
	 * @throws EntityNotFoundException levée si le thème n'existe pas
	 */
	public Ouvrage creer(OuvrageCreateDto ouvrageCreateDto) throws AlreadyExistsException, EntityNotFoundException;
	/**
	 * Méthode permettant de rechercher des ouvrages en fonction de critères.
	 * 
	 * @param critere Critère de recherche
	 * @param paging Pagination
	 * @return Une collection des ouvrages trouvés (peut être vide)
	 */
	Page<Ouvrage> rechercherOuvrages(OuvrageCriteria critere, Pageable paging);
	/**
	 * Méthode permettant d'obtenir les détails d'un ouvrage.
	 * 
	 * @param ouvrageId L'id de l'ouvrage
	 * @param utilisateurId L'id de l'utilisateur
	 * @param reqesterName L'email (login) du demandeur
	 * @return L'ouvrage trouvé
	 * @throws EntityNotFoundException levée si l'id n'existe pas
	 * @throws NotAllowedException levée si la demande ne vient pas de l'abonné ou d'un employé
	 */
	public OuvrageConsultDto consulterOuvrage(long ouvrageId, long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException;
}
