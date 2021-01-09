package com.ocdev.biblio.apibiblio.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;

/**
 * Interface d'accès au données de la classe {@link com.ocdev.biblio.apibiblio.entities.Utilisateur}.
 * Utilise JPA.
 * @author C.Orsini
 *
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>
{
	public Optional<Utilisateur> findByEmailIgnoreCase(String email);
}
