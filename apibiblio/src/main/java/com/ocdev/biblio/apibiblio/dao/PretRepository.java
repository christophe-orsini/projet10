package com.ocdev.biblio.apibiblio.dao;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ocdev.biblio.apibiblio.entities.Pret;

/**
 * Interface d'accès au données de la classe {@link com.ocdev.biblio.apibiblio.entities.Pret}.
 * Utilise JPA.
 * @author C.Orsini
 *
 */
@Repository
public interface PretRepository extends JpaRepository<Pret, Long>
{
	@Query(value = "SELECT p FROM Pret p WHERE abonne_id = ?1 AND ouvrage_id = ?2 AND (statut = 'EN_COURS' OR statut = 'PROLONGE' OR statut = 'RETARD')")
	Optional<Pret> findByAbonneIdAndOuvrageIdAndEnPret(Long abonneId, Long ouvrageId);
	
	@Query(value = "SELECT p FROM Pret p WHERE abonne_id = ?1 AND ouvrage_id = ?2 AND (statut = 'RESERVE' OR statut = 'DISPONIBLE')")
	Optional<Pret> findByAbonneIdAndOuvrageIdAndReserve(Long abonneId, Long ouvrageId);	
	
	@Query(value = "SELECT p FROM Pret p WHERE abonne_id = ?1 AND (statut = 'EN_COURS' OR statut = 'PROLONGE' OR statut = 'RETARD')")
	Page<Pret> findAllPretsByAbonneId(Long abonneId, Pageable paging);
	
	Collection<Pret> findByDateFinPrevuLessThan(Date dateMaxi);
	
	@Query(value = "SELECT p FROM Pret p WHERE ouvrage_id = ?1 AND (statut = 'RESERVE' OR statut = 'DISPONIBLE') ORDER BY date_heure_reservation")
	Collection<Pret> findAllReservationsByOuvrageId(Long ouvrageId);

	@Query(value = "SELECT p FROM Pret p WHERE abonne_id = ?1 AND (statut = 'RESERVE' OR statut = 'DISPONIBLE')")
	Collection<Pret> findAllReservationsByAbonneId(Long abonneId);
	
	@Query(value = "SELECT * FROM Pret p WHERE ouvrage_id = ?1 AND (statut = 'EN_COURS' OR statut = 'PROLONGE' OR statut = 'RETARD') ORDER BY date_fin_prevu LIMIT 1", nativeQuery = true)
	Optional<Pret> findFirstPretByOuvrageId(Long ouvrageId);
}
