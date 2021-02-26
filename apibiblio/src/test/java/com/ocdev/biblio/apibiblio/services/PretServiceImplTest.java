package com.ocdev.biblio.apibiblio.services;

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
import com.ocdev.biblio.apibiblio.entities.Statut;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.NotEnoughCopiesException;

@ExtendWith(MockitoExtension.class)
class PretServiceImplTest
{
	private PretServiceImpl pretServiceUnderTest;
	
	@Mock private PretRepository pretRepositoryMock;
	@Mock private OuvrageRepository ouvrageRepositoryMock;
	@Mock private UtilisateurRepository utilisateurRepositoryMock;
	@Mock private IDtoConverter<Pret, ReservationDto> reservationConverterMock;
	@Mock Page<Ouvrage> pageMock;
	
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
	void creer_ShouldRaiseEntityNotFoundException_WhenAbonneNotExists()
	{
		//arrange
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.creer(2L, 1L);
		}).withMessage("L'abonné n'existe pas");
	}	
	
	@Test
	void creer_ShouldRaiseEntityNotFoundException_WhenOuvrageNotExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));

		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.creer(2L, 1L);
		}).withMessage("L'ouvrage n'existe pas");
	}
	
	@Test
	void creer_ShouldRaiseAlreadyExistsException_WhenPretExists()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));

		Ouvrage ouvrage =  new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Pret pret = new Pret();
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
			.thenReturn(Optional.of(pret));
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.creer(2L, 1L);
		}).withMessage("Un prêt en cours existe déjà pour cet abonné et cet ouvrage");
	}
	
	@Test
	void creer_ShouldRaiseNotEnoughCopiesException_WhenOuvrageHaveNoExemplaires()
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));

		Ouvrage ouvrage =  new Ouvrage();
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
		.thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(NotEnoughCopiesException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.creer(2L, 1L);
		}).withMessage("Pas assez d'exemplaires pour le prêt de cet ouvrage");
	}
	
	@Test
	void creer_ShouldReturn1Exemplaire_WhenCalledWith2Exemplaires() throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));

		Ouvrage ouvrage =  new Ouvrage();
		ouvrage.setId(1L);
		ouvrage.setNbreExemplaire(2);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
		.thenReturn(Optional.empty());
		
		Mockito.when(ouvrageRepositoryMock.save(Mockito.any(Ouvrage.class))).thenReturn(ouvrage);
		
		Pret pret = new Pret();
		pret.setId(1L);
		pret.setOuvrage(ouvrage);
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(pret);
		
		// act
		Pret actual = pretServiceUnderTest.creer(2L,  pret.getId());
		
		// assert
		assertThat(actual.getOuvrage().getNbreExemplaire()).isEqualTo(1);
		Mockito.verify(ouvrageRepositoryMock, Mockito.times(1)).save(ouvrage);
	}
	
	@Test
	void creer_ShouldReturnAbonne2_WhenSuccess() throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException
	{
		//arrange
		Utilisateur abonne = new Utilisateur();
		abonne.setId(2L);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(abonne));

		Ouvrage ouvrage =  new Ouvrage();
		ouvrage.setId(1L);
		ouvrage.setNbreExemplaire(1);
		Mockito.<Optional<Ouvrage>>when(ouvrageRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(ouvrage));
		
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findByAbonneIdAndOuvrageIdAndEnPret(Mockito.anyLong(), Mockito.anyLong()))
		.thenReturn(Optional.empty());
		
		Mockito.when(ouvrageRepositoryMock.save(Mockito.any(Ouvrage.class))).thenReturn(ouvrage);
		
		Pret pret = new Pret();
		pret.setId(1L);
		pret.setAbonne(abonne);
		Mockito.when(pretRepositoryMock.save(Mockito.any(Pret.class))).thenReturn(pret);
		
		// act
		Pret actual = pretServiceUnderTest.creer(abonne.getId(),  pret.getId());
		
		// assert
		assertThat(actual.getAbonne().getId()).isEqualTo(2L);
		Mockito.verify(ouvrageRepositoryMock, Mockito.times(1)).save(ouvrage);
		Mockito.verify(pretRepositoryMock, Mockito.times(1)).save(Mockito.any(Pret.class));
	}
	
	@Test
	void retournerOuvrage_ShouldRaiseEntityNotFoundException_WhenPretNotExists()
	{
		//arrange
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retournerOuvrage(1L, 2L);
		}).withMessage("Le prêt n'existe pas");
	}
	
	@Test
	void retournerOuvrage_ShouldRaiseEntityNotFoundException_WhenPretNotActive()
	{
		//arrange
		Pret pret = new Pret();
		pret.setStatut(Statut.DISPONIBLE);
		Mockito.<Optional<Pret>>when(pretRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(pret));
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			pretServiceUnderTest.retournerOuvrage(1L, 2L);
		}).withMessage("Le prêt n'existe pas");
	}
}