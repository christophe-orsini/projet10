package com.ocdev.biblio.apibiblio.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
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
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.FullWaitingQueueException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;
import com.ocdev.biblio.apibiblio.errors.NotEnoughCopiesException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest
{
	private PretServiceImpl pretServiceUnderTest;
	
	@Mock private PretRepository pretRepositoryMock;
	@Mock private OuvrageRepository ouvrageRepositoryMock;
	@Mock private UtilisateurRepository utilisateurRepositoryMock;
	@Mock private IDtoConverter<Pret, ReservationDto> reservationConverterMock;
	@Mock Page<Pret> pageMock;
	
	@BeforeEach
	void setUp() throws Exception
	{
		 MockitoAnnotations.initMocks(this);
		 pretServiceUnderTest = new PretServiceImpl(
				 pretRepositoryMock,
		 		ouvrageRepositoryMock,
				utilisateurRepositoryMock,
				reservationConverterMock);
	}
	
	@Test
	void reserver_ShouldRaiseEntityNotFoundException_WhenAbonneNotExists()
	{
		//arrange
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, "");
		}).withMessage("L'abonné n'existe pas");
	}
	
	@Test
	void reserver_ShouldRaiseEntityNotFoundException_WhenOuvrageNotExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, "");
		}).withMessage("L'ouvrage n'existe pas");
	}
	
	@Test
	void reserver_ShouldRaiseNotAllowedException_WhenRequesterNotExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage(); 
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, "");
		}).withMessage("Vous n'etes pas correctement authentifié");
	}
	
	@Test
	void reserver_ShouldRaiseNotAllowedException_WhenRequesterIsNotAbonne()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		abonne.setEmail("abonne@biblio.fr");
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage(); 
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_ABONNE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Vous ne pouvez pas réserver cet ouvrage. Vous n'etes pas l'abonné");
	}
	
	@Test
	void reserver_ShouldRaiseAvailableException_WhenOuvrageIsDisponible()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaire(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		// act & assert
		assertThatExceptionOfType(AvailableException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Cet ouvrage est disponible");
	}
	
	@Test
	void reserver_ShouldRaiseAlreadyExistsException_WhenPretIsEnCours()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Pret pret = new Pret();
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.of(pret));
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Un prêt est en cours pour cet abonné et cet ouvrage");
	}
	
	@Test
	void reserver_ShouldRaiseAlreadyExistsException_WhenReservationExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Pret reservation = new Pret();
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndReserve(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.of(reservation));
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Une réservation existe déjà pour cet abonné et cet ouvrage");
	}
	
	@Test
	void reserver_ShouldRaiseNotEnoughCopiesException_WhenNoTotalExemplaire()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndReserve(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotEnoughCopiesException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Pas assez d'exemplaires pour le prêt de cet ouvrage");
	}
	
	@Test
	void reserver_ShouldRaiseFullWaitingQueueException_WhenQueueIsFull()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndReserve(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Collection<Pret> reservations = new ArrayList<Pret>();
		Pret reservation = new Pret();
		reservations.add(reservation);
		reservations.add(reservation);
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(reservations);
		
		// act & assert
		assertThatExceptionOfType(FullWaitingQueueException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.reserver(2L, 1L, requester.getEmail());
		}).withMessage("Nombre de réservation maximale atteinte pour cet ouvrage");
	}
	
	@Test
	void reserver_ShouldReturnNewResrvation_WhenSuccess() throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, FullWaitingQueueException, NotAllowedException, AvailableException
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndReserve(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.empty());
		
		Collection<Pret> reservations = new ArrayList<Pret>();
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(reservations);
		
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		// act
		Pret actual = pretServiceUnderTest.reserver(2L,  1L, requester.getEmail());
		
		// assert
		assertThat(actual.getStatut()).isEqualTo(Statut.RESERVE);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(Mockito.any(Pret.class));
	}
	
	@Test
	void annulerReservation_ShouldRaiseEntityNotFoundException_WhenReservationNotExists()
	{
		//arrange
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.annulerReservation(5L, 2L, "");
		}).withMessage("La réservation n'existe pas");
	}
	
	@Test
	void annulerReservation_ShouldRaiseEntityNotFoundException_WhenReservationIsPret()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.EN_COURS);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.annulerReservation(5L, 2L, "");
		}).withMessage("Ce pret n'est pas une réservation");
	}
	
	@Test
	void annulerReservation_ShouldRaiseEntityNotFoundException_WhenAbonneNotExists()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.annulerReservation(5L, 2L, "");
		}).withMessage("Le demandeur n'existe pas");
	}
	
	@Test
	void annulerReservation_ShouldRaiseNotAllowedException_WhenRequesterNotExists()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.annulerReservation(5L, 2L, "");
		}).withMessage("Vous n'etes pas correctement authentifié");
	}
	
	@Test
	void annulerReservation_ShouldRaiseNotAllowedException_WhenRequesterIsNotAbonne()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		abonne.setEmail("abonne@biblio.fr");
		reservation.setAbonne(abonne);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_ABONNE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.annulerReservation(5L, 2L, requester.getEmail());
		}).withMessage("Vous ne pouvez pas annuler cette réservation. Vous n'etes pas le demandeur");
	}
	
	@Test
	void annulerReservation_ShouldSetStatutAnnule_WhenSuccess() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setId(1L);
		reservation.setOuvrage(ouvrage);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findNextReservationsByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act
		pretServiceUnderTest.annulerReservation(5L, 2L, requester.getEmail());
		
		// assert
		assertThat(reservation.getStatut()).isEqualTo(Statut.ANNULEE);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(reservation);
	}
	
	@Test
	void annulerReservation_ShouldUpdateExemplaire_WhenReservationIsDisponible() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setId(1L);
		ouvrage.setNbreExemplaire(1);
		reservation.setOuvrage(ouvrage);
		Mockito.when(ouvrageRepositoryMock.save(Mockito.any(Ouvrage.class))).thenReturn(ouvrage);
		
		Pret otherReservation = new Pret();
		otherReservation.setOuvrage(ouvrage);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findNextReservationsByOuvrageId(Mockito.anyLong()))
			.thenReturn(Optional.of(otherReservation));
		
		// act
		pretServiceUnderTest.annulerReservation(5L, 2L, requester.getEmail());
		
		// assert
		assertThat(reservation.getStatut()).isEqualTo(Statut.ANNULEE);
		assertThat(ouvrage.getNbreExemplaire()).isEqualTo(1);
		assertThat(otherReservation.getStatut()).isEqualTo(Statut.DISPONIBLE);
		Mockito.verify(ouvrageRepositoryMock, Mockito.times(2)).save(ouvrage);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(reservation);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(otherReservation);
	}
	
	@Test
	void listerReservationAbonne_ShouldRaiseEntityNotFoundException_WhenAbonneNotExists()
	{
		//arrange
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.listerReservationsAbonne(2L, "");
		}).withMessage("L'abonné n'existe pas");
	}
	
	@Test
	void listerReservationAbonne_ShouldRaiseNotAllowedException_WhenRequesterNotExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
			
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.listerReservationsAbonne(2L, "");
		}).withMessage("Vous n'etes pas correctement authentifié");
	}
	
	@Test
	void listerReservationAbonne_ShouldRaiseNotAllowedException_WhenRequesterIsNotAbonne()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		abonne.setEmail("abonne@biblio.fr");
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_ABONNE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.listerReservationsAbonne(2L, requester.getEmail());
		}).withMessage("Vous ne pouvez pas lister les réservations de cet abonné");
	}
	
	@Test
	void listerReservationAbonne_ShouldReturnEmpty_WhenNoReservations() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Collection<Pret> reservations = new ArrayList<Pret>();
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsByAbonneId(Mockito.anyLong())).thenReturn(reservations);
		
		// act
		Collection<ReservationDto> results = pretServiceUnderTest.listerReservationsAbonne(2L, requester.getEmail());
		
		// assert
		assertThat(results.size()).isEqualTo(0);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsByAbonneId(Mockito.anyLong());
	}
	
	@Test
	void listerReservationAbonne_ShouldReturnReservations_WhenSuccess() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Collection<Pret> reservations = new ArrayList<Pret>();
		Pret reservation = new Pret();
		reservations.add(reservation);
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsByAbonneId(Mockito.anyLong())).thenReturn(reservations);
		
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(reservations);
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setId(1L);
		reservation.setOuvrage(ouvrage);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		ReservationDto dto = new ReservationDto();
		Mockito.when(reservationConverterMock.convertEntityToDto(Mockito.any(Pret.class))).thenReturn(dto);
		
		// act
		Collection<ReservationDto> results = pretServiceUnderTest.listerReservationsAbonne(2L, requester.getEmail());
		dto = (ReservationDto)results.toArray()[0];
		
		// assert
		assertThat(results.size()).isEqualTo(1);
		assertThat(dto.getRang()).isEqualTo(1);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsByAbonneId(Mockito.anyLong());
	}
	
	@Test
	void retirerReservation_ShouldRaiseEntityNotFoundException_WhenReservationNotExists()
	{
		//arrange
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retirerReservation(5L, 2L, "");
		}).withMessage("La réservation n'existe pas");
	}
	
	@Test
	void retirerReservation_ShouldRaiseEntityNotFoundException_WhenStatutIsNotDisponible()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retirerReservation(5L, 2L, "");
		}).withMessage("L'ouvrage n'est pas disponible");
	}
	
	@Test
	void retirerReservation_ShouldRaiseEntityNotFoundException_WhenAbonneNotExists()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retirerReservation(5L, 2L, "");
		}).withMessage("Le demandeur n'existe pas");
	}
	
	@Test
	void retirerReservation_ShouldRaiseNotAllowedException_WhenRequesterNotExists()
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retirerReservation(5L, 2L, "");
		}).withMessage("Vous n'etes pas correctement authentifié");
	}
	
	@Test
	void retirerReservation_ShouldRaiseNotAllowedException_WhenRequesterIsNotAbonne()
	{
		//arrange
		
		Pret reservation = new Pret();
		reservation.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		abonne.setEmail("abonne@biblio.fr");
		reservation.setAbonne(abonne);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_ABONNE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retirerReservation(5L, 2L, requester.getEmail());
		}).withMessage("Vous ne pouvez pas retirer cette réservation. Vous n'etes pas l'abonné");
	}
	
	@Test
	void retirerReservation_ShouldSetStatutEnCours_WhenSuccess() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		
		Pret reservation = new Pret();
		reservation.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		// act
		pretServiceUnderTest.retirerReservation(5L, 2L, requester.getEmail());
		
		// assert
		assertThat(reservation.getStatut()).isEqualTo(Statut.EN_COURS);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(reservation);
	}
	
	@Test
	void reservationsDisponibles_ShouldReturnEmpty_WhenNoReservations()
	{
		// arrange
		Collection<Pret> reservations = new ArrayList<Pret>();
		Mockito.<Collection<Pret>>when(pretRepositoryMock.findAllReservationsDisponibles()).thenReturn(reservations);
		
		// act
		Collection<Pret> results = pretServiceUnderTest.reservationsDisponibles();
		
		// assert
		assertThat(results.size()).isEqualTo(0);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsDisponibles();
	}
	
	@Test
	void setEmailsEnvoyes_ShouldDoNothing_WhenCalledWithNull()
	{
		// arrange
		
		// act
		pretServiceUnderTest.setEmailsEnvoyes(null);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.never()).findById(Mockito.anyLong());	
	}
	
	@Test
	void setEmailsEnvoyes_ShouldDoNothing_WhenCalledWithEmpty()
	{
		// arrange
		Collection<Long> reservationIDs = new ArrayList<Long>();
		
		// act
		pretServiceUnderTest.setEmailsEnvoyes(reservationIDs);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.never()).findById(Mockito.anyLong());	
	}
	
	@Test
	void setEmailsEnvoyes_ShouldDoNothing_WhenReservationNotFound()
	{
		// arrange
		Collection<Long> reservationIDs = new ArrayList<Long>();
		reservationIDs.add(5L);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act
		pretServiceUnderTest.setEmailsEnvoyes(reservationIDs);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findById(5L);	
	}
	
	@Test
	void setEmailsEnvoyes_ShouldSetEmail_WhenSuccess()
	{
		// arrange
		Collection<Long> reservationIDs = new ArrayList<Long>();
		reservationIDs.add(5L);
		reservationIDs.add(10L);
		
		Pret reservation = new Pret();
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		// act
		pretServiceUnderTest.setEmailsEnvoyes(reservationIDs);
		
		// assert
		assertThat(reservation.isEmailEnvoye()).isTrue();
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findById(5L);	
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findById(10L);	
		Mockito.verify(pretRepositoryMock, Mockito.times(2)).save(Mockito.any(Pret.class));	
	}
	
	@Test
	void annulerReservations_ShouldDoNothing_WhenCalledWithNull()
	{
		// arrange
		
		// act
		pretServiceUnderTest.annulerReservations(null);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.never()).findById(Mockito.anyLong());	
	}
	
	@Test
	void annulerReservations_ShouldDoNothing_WhenCalledWithEmpty()
	{
		// arrange
		Collection<Long> reservationIDs = new ArrayList<Long>();
		
		// act
		pretServiceUnderTest.annulerReservations(reservationIDs);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.never()).findById(Mockito.anyLong());	
	}
	
	@Test
	void annulerReservations_ShouldDoNothing_WhenReservationNotFound()
	{
		// arrange
		Collection<Long> reservationIDs = new ArrayList<Long>();
		reservationIDs.add(5L);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act
		pretServiceUnderTest.annulerReservations(reservationIDs);
		
		// assert
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findById(5L);	
	}
	
	@Test
	void annulerReservations_ShouldSetStatutAnnule_WhenSuccess() throws EntityNotFoundException, NotAllowedException
	{
		//arrange
		Pret reservation = new Pret();
		reservation.setId(5L);
		reservation.setStatut(Statut.RESERVE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));
		
		Utilisateur requester = new Utilisateur();
		requester.setId(2L);
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_EMPLOYE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));
		
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(reservation);
		
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setId(1L);
		reservation.setOuvrage(ouvrage);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findNextReservationsByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Collection<Long> reservationIDs = new ArrayList<Long>();
		reservationIDs.add(5L);
		
		reservation.setAbonne(requester);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
		
		// act
		pretServiceUnderTest.annulerReservations(reservationIDs);
		
		// assert
		assertThat(reservation.getStatut()).isEqualTo(Statut.ANNULEE);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(reservation);
		Mockito.verify(pretRepositoryMock, Mockito.times(2)).findById(5L);	
	}
}