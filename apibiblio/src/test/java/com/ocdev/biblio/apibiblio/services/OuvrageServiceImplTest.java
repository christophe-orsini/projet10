package com.ocdev.biblio.apibiblio.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.criterias.OuvrageCriteria;
import com.ocdev.biblio.apibiblio.criterias.OuvrageSpecification;
import com.ocdev.biblio.apibiblio.dao.OuvrageRepository;
import com.ocdev.biblio.apibiblio.dao.PretRepository;
import com.ocdev.biblio.apibiblio.dao.ThemeRepository;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Role;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;

@ExtendWith(MockitoExtension.class)
class OuvrageServiceImplTest
{
	private OuvrageServiceImpl ouvrageServiceUnderTest;
	
	@Mock private OuvrageRepository ouvrageRepositoryMock;
	@Mock private ThemeRepository themeRepositoryMock;
	@Mock private UtilisateurRepository utilisateurRepositoryMock;
	@Mock private PretRepository pretRepositoryMock;
	@Mock private IDtoConverter<Ouvrage, OuvrageCreateDto> ouvrageCreateConverterMock;
	@Mock private IDtoConverter<Ouvrage, OuvrageConsultDto> ouvrageConsultConverterMock;
	@Mock Page<Ouvrage> pageMock;
	
	@BeforeEach
	void setUp() throws Exception
	{
		 MockitoAnnotations.initMocks(this);
		 ouvrageServiceUnderTest = new OuvrageServiceImpl(
		 		ouvrageRepositoryMock,
				themeRepositoryMock,
				utilisateurRepositoryMock,
				pretRepositoryMock,
				ouvrageCreateConverterMock,
				ouvrageConsultConverterMock);
	}
	
	@Test
	void creer_ShouldRaiseAlreadyExistsException_WhenOuvrageExists()
	{
		//arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findByTitreIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(ouvrage));
		
		OuvrageCreateDto ouvrageDto = new OuvrageCreateDto();
		ouvrageDto.setTitre("Titre");
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.creer(ouvrageDto);
		}).withMessage("Un ouvrage avec le même titre existe déjà");
	}
	
	@Test
	void creer_ShouldRaiseEntityNotFoundException_WhenThemeNotExists()
	{
		//arrange
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findByTitreIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		OuvrageCreateDto ouvrageDto = new OuvrageCreateDto();
		ouvrageDto.setTitre("Titre");
		ouvrageDto.setTheme(999L);
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.creer(ouvrageDto);
		}).withMessage("Ce thème n'existe pas");
	}
	
	@Test
	void creer_ShouldReturnNewOuvrage_WhenOuvrageNotExists() throws AlreadyExistsException, EntityNotFoundException
	{
		//arrange
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findByTitreIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		Theme theme = new Theme("Theme");
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(theme));
		
		OuvrageCreateDto ouvrageDto = new OuvrageCreateDto();
		ouvrageDto.setTitre("Titre");
		ouvrageDto.setTheme(999L);
		Ouvrage ouvrage = new Ouvrage("Titre", null, 0, null);
		Mockito.when(ouvrageCreateConverterMock.convertDtoToEntity(Mockito.any(OuvrageCreateDto.class))).thenReturn(ouvrage);
		
		Mockito.when(ouvrageRepositoryMock.save(Mockito.any(Ouvrage.class))).thenReturn(ouvrage);
		
		// act
		Ouvrage actual = ouvrageServiceUnderTest.creer(ouvrageDto);
		
		// assert
		assertThat(actual.getTitre()).isEqualTo(ouvrageDto.getTitre());
		Mockito.verify(ouvrageRepositoryMock, Mockito.times(1)).save(ouvrage);
	}
	
	@Test
	void rechercherOuvrages_ShouldCallFindAll()
	{
		// arrange
		OuvrageCriteria critere = new OuvrageCriteria(null, null, 0, null, 0);
		Pageable pageable = PageRequest.of(0, 8);
		Mockito.when(ouvrageRepositoryMock.findAll(Mockito.any(OuvrageSpecification.class), Mockito.any(Pageable.class))).thenReturn(pageMock);
		
		// act
		ouvrageServiceUnderTest.rechercherOuvrages(critere, pageable);
		
		// assert
		Mockito.verify(ouvrageRepositoryMock, Mockito.times(1)).findAll(Mockito.any(OuvrageSpecification.class), Mockito.any(Pageable.class));
	}
	
	@Test
	void consulterOuvrage_ShouldRaiseEntityNotFoundException_WhenOuvrageNotExists()
	{
		// arrange
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.consulterOuvrage(1L, 2L, "");
		}).withMessage("L'ouvrage n'existe pas");
	}
	
	@Test
	void consulterOuvrage_ShouldRaiseEntityNotFoundException_WhenUtilisateurNotExists()
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.consulterOuvrage(1L, 2L, "");
		}).withMessage("L'utilisateur n'existe pas");
	}
	
	@Test
	void consulterOuvrage_ShouldRaiseNotAllowedException_WhenRequesterNotExists()
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));

		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());

		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.consulterOuvrage(1L, 2L, "");
		}).withMessage("Vous n'etes pas correctement authentifié");
	}
	
	@Test
	void consulterOuvrage_ShouldRaiseNotAllowedException_WhenRequesterIsNotAbonne()
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("abonne@biblio.fr");
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));

		Utilisateur requester = new Utilisateur();
		requester.setEmail("dummy@domain.tld");
		requester.setRole(Role.ROLE_ABONNE);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(requester));

		// act & assert
		assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() ->
		{
			ouvrageServiceUnderTest.consulterOuvrage(1L, 2L, requester.getEmail());
		}).withMessage("Vous ne pouvez pas consulter cet ouvrage. Vous n'etes pas l'abonné");
	}
	
	@Test
	void consulterOuvrage_ShouldReturnProchainRetourToday_WhenNoPret() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Date date = new Date();
		ouvrageDto.setProchainRetour(date);
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(new ArrayList<Pret>());
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		assertThat(sdf.format(actual.getProchainRetour())).isEqualTo(sdf.format(date));
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findFirstPretByOuvrageId(1L);
	}
	
	@Test
	void consulterOuvrage_ShouldProchainRetourEqualsPretRetour_WhenPretExists() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		@SuppressWarnings("deprecation")
		Date date = new Date(2020, 02, 25);
		ouvrageDto.setProchainRetour(date);
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Pret pret = new Pret();
		pret.setDateFinPrevu(date);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.of(pret));
		
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(new ArrayList<Pret>());
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.getProchainRetour()).isEqualTo(date);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findFirstPretByOuvrageId(1L);
	}
	
	@Test
	void consulterOuvrage_ShouldReturnNbreReservations_WhenReservationExists() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Collection<Pret> prets = new ArrayList<Pret>();
		prets.add(new Pret());
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(prets);
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.getNbreReservations()).isEqualTo(1);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsByOuvrageId(1L);
		Mockito.verify(pretRepositoryMock, Mockito.never()).findByAbonneIdAndOuvrageIdAndEnPretOrReserve(2L, 1L);
	}
	
	@Test
	void consulterOuvrage_ShouldNotBeReservable_WhenDisponible() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		ouvrage.setNbreExemplaire(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(new ArrayList<Pret>());
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.isReservable()).isFalse();
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsByOuvrageId(1L);
		Mockito.verify(pretRepositoryMock, Mockito.never()).findByAbonneIdAndOuvrageIdAndEnPretOrReserve(2L, 1L);
	}

	@Test
	void consulterOuvrage_ShouldNotBeReservable_WhenReachMaxReservation() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Collection<Pret> prets = new ArrayList<Pret>();
		prets.add(new Pret());
		prets.add(new Pret());
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(prets);
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.isReservable()).isFalse();
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findAllReservationsByOuvrageId(1L);
		Mockito.verify(pretRepositoryMock, Mockito.never()).findByAbonneIdAndOuvrageIdAndEnPretOrReserve(2L, 1L);
	}
	
	@Test
	void consulterOuvrage_ShouldNotBeReservable_WhenPretAlreadyExists() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(new ArrayList<Pret>());
		
		Pret pret = new Pret();
		Mockito.when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPretOrReserve(Mockito.anyLong(), Mockito.anyLong())).
			thenReturn(Optional.of(pret));
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.isReservable()).isFalse();
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findByAbonneIdAndOuvrageIdAndEnPretOrReserve(2L, 1L);
	}
	
	@Test
	void consulterOuvrage_ShouldBeReservable() throws EntityNotFoundException, NotAllowedException
	{
		// arrange
		Ouvrage  ouvrage = new Ouvrage();
		ouvrage.setNbreExemplaireTotal(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail("dummy@domain.tld");
		utilisateur.setRole(Role.ROLE_ADMINISTRATEUR);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(utilisateur));
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(utilisateur));
		
		OuvrageConsultDto ouvrageDto = new OuvrageConsultDto();
		Mockito.when(ouvrageConsultConverterMock.convertEntityToDto(Mockito.any(Ouvrage.class))).thenReturn(ouvrageDto);
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findFirstPretByOuvrageId(Mockito.anyLong())).thenReturn(Optional.empty());
		
		Mockito.when(pretRepositoryMock.findAllReservationsByOuvrageId(Mockito.anyLong())).thenReturn(new ArrayList<Pret>());
		
		Mockito.when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPretOrReserve(Mockito.anyLong(), Mockito.anyLong())).
			thenReturn(Optional.empty());
		
		// act
		OuvrageConsultDto actual = ouvrageServiceUnderTest.consulterOuvrage(1L,  2L, utilisateur.getEmail());

		// assert
		assertThat(actual.isReservable()).isTrue();
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).findByAbonneIdAndOuvrageIdAndEnPretOrReserve(2L, 1L);
	}
}
