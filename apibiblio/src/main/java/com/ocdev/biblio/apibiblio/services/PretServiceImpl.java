 package com.ocdev.biblio.apibiblio.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.dao.OuvrageRepository;
import com.ocdev.biblio.apibiblio.dao.PretRepository;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Role;
import com.ocdev.biblio.apibiblio.entities.Statut;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.AvailableException;
import com.ocdev.biblio.apibiblio.errors.DelayLoanException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.FullWaitingQueueException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;
import com.ocdev.biblio.apibiblio.errors.NotEnoughCopiesException;
import com.ocdev.biblio.apibiblio.utils.AppSettings;

@Service
public class PretServiceImpl implements PretService
{
	private PretRepository pretRepository;
	private OuvrageRepository ouvrageRepository;
	private UtilisateurRepository utilisateurRepository;
	private IDtoConverter<Pret, ReservationDto> reservationConverter;
	
	public PretServiceImpl(
			@Autowired PretRepository pretRepository,
			@Autowired OuvrageRepository ouvrageRepository,
			@Autowired UtilisateurRepository utilisateurRepository,
			@Autowired IDtoConverter<Pret, ReservationDto> reservationConverter)
	{
		this.pretRepository = pretRepository;
		this.ouvrageRepository = ouvrageRepository;
		this.utilisateurRepository = utilisateurRepository;
		this.reservationConverter = reservationConverter;
	}
	
	@Override
	@Transactional
	public Pret creer(Long abonneId, Long ouvrageId, String requesterName) throws AlreadyExistsException, EntityNotFoundException,
		NotEnoughCopiesException, NotAllowedException
	{
		// verfifier si l'abonné existe
		Optional<Utilisateur> abonne = utilisateurRepository.findById(abonneId);
		if (!abonne.isPresent()) throw new EntityNotFoundException("L'abonné n'existe pas");
				
		// verfifier si l'ouvrage existe
		Optional<Ouvrage> ouvrage = ouvrageRepository.findById(ouvrageId);
		if (!ouvrage.isPresent()) throw new EntityNotFoundException("L'ouvrage n'existe pas");
		
		// recherche si un pret en cours existe deja
		Optional<Pret> pretExists = pretRepository.findByAbonneIdAndOuvrageIdAndEnPret(abonneId, ouvrageId);
		if (pretExists.isPresent()) throw new AlreadyExistsException("Un prêt en cours existe déjà pour cet abonné et cet ouvrage");		
			
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && !abonne.get().getEmail().equals(requesterName))
			throw new NotAllowedException("Vous ne pouvez pas créer un pret pour un autre abonné");
				
		// verifier s'il y a assez d'exemplaires d'ouvrage
		if (ouvrage.get().getNbreExemplaire() < 1) throw new NotEnoughCopiesException("Pas assez d'exemplaires pour le prêt de cet ouvrage");
		
		// mise à jour du nombre d'exemplaires
		ouvrage.get().setNbreExemplaire(ouvrage.get().getNbreExemplaire() - 1);
		
		// initialisation du pret
		Pret pret = new Pret(abonne.get(), ouvrage.get());
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		pret.setDateDebut(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, AppSettings.getIntSetting("duree-pret"));
		pret.setDateFinPrevu(c.getTime());
		
		pret.setStatut(Statut.EN_COURS);
		
		// Initialiser le nombre de prolongations et de periode possible
		pret.setProlongationsPossible(AppSettings.getIntSetting("nbre-prolongations"));
		pret.setPeriodes(1);
		
		// sauvegarde de l'ouvrage
		ouvrageRepository.save(ouvrage.get());
		
		// creation du pret
		return pretRepository.save(pret);
	}

	@Override
	@Transactional
	public void retournerOuvrage(Long pretId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		Optional<Pret> pret = pretRepository.findByIdAndEnPret(pretId);
		if (!pret.isPresent()) throw new EntityNotFoundException("Le prêt n'existe pas");
		
		// verifier si l'abonne existe
		Optional<Utilisateur> abonne = utilisateurRepository.findById(utilisateurId);
		if (!abonne.isPresent()) throw new EntityNotFoundException("L'abonné n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && 
				(abonne.get().getId() != pret.get().getAbonne().getId() || !pret.get().getAbonne().getEmail().equals(requesterName)))
			throw new NotAllowedException("Vous ne pouvez pas retourner ce prêt. Vous n'etes pas l'emprunteur");
				
		// mettre a jour le nombre d'exemplaires
		Ouvrage ouvrage = pret.get().getOuvrage();
		ouvrage.setNbreExemplaire(ouvrage.getNbreExemplaire() + 1);
		// sauvegarde de l'ouvrage
		ouvrageRepository.save(ouvrage);
				
		// set date de retour
		pret.get().setDateRetour(new Date());
		// changer statut
		pret.get().setStatut(Statut.RETOURNE);
		// sauvegarder
		pretRepository.save(pret.get());
		
		// recherche de la prochaine reservation
		prochaineReservation(pret.get().getOuvrage().getId());
	}

	@Override
	public Pret prolonger(Long pretId, Long utilisateurId, String requesterName) throws EntityNotFoundException, DelayLoanException, NotAllowedException
	{
		Optional<Pret> pret = pretRepository.findByIdAndEnPret(pretId);
		if (!pret.isPresent()) throw new EntityNotFoundException("Le prêt n'existe pas");
		
		// verifier si le pret peut etre prolongé
		if (pret.get().getProlongationsPossible() <= 0) throw new DelayLoanException("Le prêt ne peut plus être prolongé");
			
		// verifier si le pret est en retard
		LocalDate now = LocalDate.now();
		LocalDate finPrevu = Instant.ofEpochMilli(pret.get().getDateFinPrevu().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		if (finPrevu.isBefore(now)) throw new DelayLoanException("Le prêt ne peut plus être prolongé car il est en retard");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && 
				(pret.get().getAbonne().getId() != utilisateurId || !pret.get().getAbonne().getEmail().equals(requesterName)))
			throw new NotAllowedException("Vous ne pouvez pas prolonger ce prêt. Vous n'etes pas l'emprunteur");
		
		// prolongation
		Calendar c = Calendar.getInstance();
		c.setTime(pret.get().getDateDebut());
		c.add(Calendar.DAY_OF_MONTH, AppSettings.getIntSetting("duree-pret") * (pret.get().getPeriodes() + 1));
		Date nouvelleDateFin = c.getTime();
		
		// MaJ pret
		pret.get().setDateFinPrevu(nouvelleDateFin);
		pret.get().setStatut(Statut.PROLONGE);
		pret.get().setPeriodes(pret.get().getPeriodes() + 1);
		pret.get().setProlongationsPossible(pret.get().getProlongationsPossible() - 1);
		
		// sauvegarder
		return pretRepository.save(pret.get());
	}

	@Override
	public Page<Pret> listerSesPrets(Long abonneId, Pageable paging, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		// verifier si l'abonné existe
		Optional<Utilisateur> abonne = utilisateurRepository.findById(abonneId);
		if (!abonne.isPresent()) throw new EntityNotFoundException("L'abonné n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && (requester.getId() != abonneId || !abonne.get().getEmail().equals(requesterName)))
			throw new NotAllowedException("Vous ne pouvez pas lister les prêts d'un autre abonné");
		
		return pretRepository.findAllPretsByAbonneId(abonneId, paging);
	}

	@Override
	public Pret consulter(Long pretId) throws EntityNotFoundException
	{
		Optional<Pret> pret = pretRepository.findById(pretId);
		if (!pret.isPresent()) throw new EntityNotFoundException("Le prêt n'existe pas");
		
		return pret.get();
	}

	@Override
	public Collection<Pret> pretsEnRetard(Date dateMaxi)
	{
		if (dateMaxi == null)
		{
			dateMaxi = new Date();
		}
		return pretRepository.findByDateFinPrevuLessThan(dateMaxi);
	}
	
	@Override
	@Transactional
	public Pret reserver(Long abonneId, Long ouvrageId, String requesterName)
			throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, FullWaitingQueueException, NotAllowedException, AvailableException
	{
		// verfifier si l'abonné existe
		Optional<Utilisateur> abonne = utilisateurRepository.findById(abonneId);
		if (!abonne.isPresent()) throw new EntityNotFoundException("L'abonné n'existe pas");
				
		// verifier si l'ouvrage existe
		Optional<Ouvrage> ouvrage = ouvrageRepository.findById(ouvrageId);
		if (!ouvrage.isPresent()) throw new EntityNotFoundException("L'ouvrage n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && !abonne.get().getEmail().equals(requesterName))
			throw new NotAllowedException("Vous ne pouvez pas réserver cet ouvrage. Vous n'etes pas l'abonné");
				
		// vérifier que l'ouvrage est indisponible RG8
		if (ouvrage.get().getNbreExemplaire() > 0) throw new AvailableException("Cet ouvrage est disponible");
		
		// recherche si un pret en cours existe deja RG3
		Optional<Pret> pretExists = pretRepository.findByAbonneIdAndOuvrageIdAndEnPret(abonneId, ouvrageId);
		if (pretExists.isPresent()) throw new AlreadyExistsException("Un prêt est en cours pour cet abonné et cet ouvrage");
		
		// recherche si une réservation existe deja
		Optional<Pret> reservationExists = pretRepository.findByAbonneIdAndOuvrageIdAndReserve(abonneId, ouvrageId);
		if (reservationExists.isPresent()) throw new AlreadyExistsException("Une réservation existe déjà pour cet abonné et cet ouvrage");		
		
		// verifier qu'il existe au moins un exemplaire pour l'ouvrage
		if (ouvrage.get().getNbreExemplaireTotal() < 1) throw new NotEnoughCopiesException("Pas assez d'exemplaires pour le prêt de cet ouvrage");
		
		// verifier que la file d'attente n'est pas pleine RG2
		int nbreReservation = pretRepository.findAllReservationsByOuvrageId(ouvrageId).size();
		int nbreMaxiReservation = ouvrage.get().getNbreExemplaireTotal() * AppSettings.getIntSetting("reservation.multiple");
		if (nbreReservation >= nbreMaxiReservation) 
			{
			throw new FullWaitingQueueException("Nombre de réservation maximale atteinte pour cet ouvrage");
			}
		
		// initialisation du pret
		Pret pret = new Pret(abonne.get(), ouvrage.get());
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		pret.setDateHeureReservation(c.getTime());
		
		pret.setStatut(Statut.RESERVE);
		
		// creation du pret
		return pretRepository.save(pret);
	}
	
	@Override
	@Transactional
	public void annulerReservation(Long reservationId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		Optional<Pret> reservation = pretRepository.findById(reservationId);
		if (!reservation.isPresent()) throw new EntityNotFoundException("La réservation n'existe pas");
		
		// verifier si la réservation est toujours en cours
		if (!reservation.get().getStatut().isReserve()) throw new EntityNotFoundException("Ce pret n'est pas une réservation");;
		
		// verifier si le demandeur existe
		Optional<Utilisateur> demandeur = utilisateurRepository.findById(utilisateurId);
		if (!demandeur.isPresent()) throw new EntityNotFoundException("Le demandeur n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && 
				(demandeur.get().getId() != reservation.get().getAbonne().getId() || !demandeur.get().getEmail().equals(requesterName)))
			throw new NotAllowedException("Vous ne pouvez pas annuler cette réservation. Vous n'etes pas le demandeur");
			
		// si reservation disponible, corriger le stock
		if (reservation.get().getStatut() == Statut.DISPONIBLE)
		{
			Ouvrage ouvrage = reservation.get().getOuvrage();
			ouvrage.setNbreExemplaire(ouvrage.getNbreExemplaire() + 1);
			ouvrageRepository.save(ouvrage);
		}
		
		// set date d'annulation
		reservation.get().setDateHeureReservation(new Date());
		// changer statut
		reservation.get().setStatut(Statut.ANNULEE);
		// sauvegarder
		pretRepository.save(reservation.get());
		
		// verifier si une autre reservation peut etre honorée
		prochaineReservation(reservation.get().getOuvrage().getId());
	}
	
	@Override
	public Collection<ReservationDto> listerReservationsAbonne(Long abonneId, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		// verifier si l'abonné existe
		Optional<Utilisateur> abonne = utilisateurRepository.findById(abonneId);
		if (!abonne.isPresent()) throw new EntityNotFoundException("L'abonné n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && !abonne.get().getEmail().equals(requesterName))
			throw new NotAllowedException("Vous ne pouvez pas lister les réservations de cet abonné");
				
		Collection<ReservationDto> results = new ArrayList<ReservationDto>();
		
		Collection<Pret> reservations = pretRepository.findAllReservationsByAbonneId(abonneId);
		for (Pret reservation : reservations)
		{
			List<Pret> ouvragesReserves = (List<Pret>) pretRepository.findAllReservationsByOuvrageId(reservation.getOuvrage().getId());
			int rang = ouvragesReserves.indexOf(reservation);
						
			Optional<Pret> prochainRetour = pretRepository.findFirstPretByOuvrageId(reservation.getOuvrage().getId());
			
			ReservationDto reservationDto = reservationConverter.convertEntityToDto(reservation);
			reservationDto.setRang(++rang);
			if (prochainRetour.isPresent())
			{
				reservationDto.setDateDisponible(prochainRetour.get().getDateFinPrevu());
			}
			else
			{
				reservationDto.setDateDisponible(new Date());
			}
			
			results.add(reservationDto);
		}
		
		return results;
	}
	
	@Override
	@Transactional
	public Pret retirerReservation(Long reservationId, Long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		Optional<Pret> reservation = pretRepository.findById(reservationId);
		if (!reservation.isPresent()) throw new EntityNotFoundException("La réservation n'existe pas");
		
		// verifier si la réservation est disponible
		if (reservation.get().getStatut() != Statut.DISPONIBLE) throw new EntityNotFoundException("L'ouvrage n'est pas disponible");
		
		// verifier si le demandeur existe
		Optional<Utilisateur> demandeur = utilisateurRepository.findById(utilisateurId);
		if (!demandeur.isPresent()) throw new EntityNotFoundException("Le demandeur n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && 
				(demandeur.get().getId() != reservation.get().getAbonne().getId() ||!demandeur.get().getEmail().equals(requesterName)))
		{
			throw new NotAllowedException("Vous ne pouvez pas retirer cette réservation. Vous n'etes pas l'abonné");
		}
			
		// changer statut
		reservation.get().setStatut(Statut.EN_COURS);
		
		// set dates pret et retour prevue
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		reservation.get().setDateDebut(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, AppSettings.getIntSetting("duree-pret"));
		reservation.get().setDateFinPrevu(c.getTime());
		
		// Initialiser le nombre de prolongations et de periode possible
		reservation.get().setProlongationsPossible(AppSettings.getIntSetting("nbre-prolongations"));
		reservation.get().setPeriodes(1);
		
		// sauvegarder
		return pretRepository.save(reservation.get());
	}
	
	@Override
	public Collection<Pret> reservationsDisponibles()
	{
		return pretRepository.findAllReservationsDisponibles();
	}
	
	@Override
	@Transactional
	public void setEmailsEnvoyes(Collection<Long> reservationIDs)
	{
		if (reservationIDs == null || reservationIDs.size() == 0) return;
		
		for (Long item : reservationIDs)
		{
			Optional<Pret> reservation = pretRepository.findById(item);
			if (reservation.isPresent())
			{
				reservation.get().setEmailEnvoye(true);
				pretRepository.save(reservation.get());	
			}
		}
	}
	
	@Override
	public void annulerReservations(Collection<Long> reservationIDs)
	{
		if (reservationIDs == null || reservationIDs.size() == 0) return;
		
		for (Long item : reservationIDs)
		{
			Optional<Pret> reservation = pretRepository.findById(item);
			if (reservation.isPresent())
			{
				try
				{
					annulerReservation(reservation.get().getId(), reservation.get().getAbonne().getId(), reservation.get().getAbonne().getEmail());	
				} 
				catch (EntityNotFoundException e)
				{
					// Ne devrait pas se produire car la réservation est persistée
				}
				catch (NotAllowedException e)
				{
					// Ne devrait pas se produire car la réservation est annulée par son auteur
				}
			}
		}
	}
	
	private void prochaineReservation(Long ouvrageId)
	{
		// Chercher la prochaine réservation
		Optional<Pret> reservation = pretRepository.findNextReservationsByOuvrageId(ouvrageId);
		if (!reservation.isPresent()) return; // plus de réservations
		
		// Mettre la date d'expiration
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR, AppSettings.getIntSetting("reservation.attente"));
		reservation.get().setDateHeureExpiration(c.getTime());
		
		// Changer le statut
		reservation.get().setStatut(Statut.DISPONIBLE);
		
		// mettre a jour le nombre d'exemplaires
		Ouvrage ouvrage = reservation.get().getOuvrage();
		ouvrage.setNbreExemplaire(ouvrage.getNbreExemplaire() - 1);
		// sauvegarde de l'ouvrage
		ouvrageRepository.save(ouvrage);
			
		// Sauvergarder
		pretRepository.save(reservation.get());
	}
}
