package com.ocdev.biblio.apibiblio.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Statut;

/**
 * Interface d'accès au données de la classe {@link com.ocdev.biblio.apibiblio.entities.Pret}.
 * Utilise JPA.
 * @author C.Orsini
 *
 */
@Repository
public interface PretRepository extends JpaRepository<Pret, Long>
{
	Optional<Pret> findByAbonneIdAndOuvrageIdAndStatutNot(Long abonneId, Long ouvrageId, Statut statut);
	Page<Pret> findByAbonneIdAndStatutNotAndStatutNot(Long abonneId, Statut statut1, Statut Statut2, Pageable paging);
	Collection<Pret> findByDateFinPrevuLessThan(Date dateMaxi);
}
