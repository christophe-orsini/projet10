package com.ocdev.biblio.apibiblio.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ocdev.biblio.apibiblio.entities.Theme;

/**
 * Interface d'accès au données de la classe {@link com.ocdev.biblio.apibiblio.entities.Theme}.
 * Utilise JPA.
 * @author C.Orsini
 *
 */
@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>
{
	public Optional<Theme> findByNomIgnoreCase(String nom);
	public Collection<Theme> findAllByNomContainsIgnoreCase(String nom);
}
