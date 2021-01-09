package com.ocdev.biblio.apibiblio.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ocdev.biblio.apibiblio.criterias.OuvrageCriteria;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

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
	 */
	public Ouvrage creer(OuvrageCreateDto ouvrageCreateDto) throws AlreadyExistsException;
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
	 * @param id L'id de l'ouvrage
	 * @return L'ouvrage trouvé
	 * @throws EntityNotFoundException levée si l'id n'existe pas
	 */
	public Ouvrage consulterOuvrage(Long id) throws EntityNotFoundException;

}
