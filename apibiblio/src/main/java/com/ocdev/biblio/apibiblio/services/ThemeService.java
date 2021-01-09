package com.ocdev.biblio.apibiblio.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

/**
 * Interface de déclaration des services pour les themes
 * @author C.Orsini
 */
public interface ThemeService
{
	/**
	 * Création d'un nouveau thème.
	 * @param themeCreateDto Le thème à créer
	 * @return Le thème créé
	 * @throws AlreadyExistsException levée si le thème existe déjà
	 */
	public Theme creer(ThemeCreateDto themeCreateDto) throws AlreadyExistsException;
	
	/**
	 * Permet d'obtenir un thème.
	 * @param id L'ID du thème
	 * @return Le thème correspondant à l'ID
	 * @throws EntityNotFoundException levée si le thème n'existe pas
	 */
	public Theme obtenir(Long id) throws EntityNotFoundException;
	/**
	 * Permet d'obtenir un thème.
	 * @param nom Le nom du thème
	 * @return Le thème correspondant à l'ID
	 * @throws EntityNotFoundException levée si le thème n'existe pas
	 */
	public Theme obtenir(String nom) throws EntityNotFoundException;
	/**
	 * Liste tous les thèmes
	 * @param paging Pagination
	 * @return La liste (peut être vide)
	 */
	public Page<Theme> listeThemes(Pageable paging);
}
