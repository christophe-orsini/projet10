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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.UtilisateurCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceImplTest
{
	private UtilisateurServiceImpl utilisateurServiceUnderTest;
	
	@Mock private UtilisateurRepository utilisateurRepositoryMock;
	@Mock private IDtoConverter<Utilisateur, UtilisateurCreateDto> utilisateurCreateConverterMock;
	@Mock private BCryptPasswordEncoder passwordEncoderMock;
	@Mock Page<Theme> pageMock;
	
	@BeforeEach
	void setUp() throws Exception
	{
		 MockitoAnnotations.initMocks(this);
		 utilisateurServiceUnderTest = new UtilisateurServiceImpl(
				 utilisateurRepositoryMock,
				 utilisateurCreateConverterMock,
				 passwordEncoderMock);
	}
	
	@Test
	void creer_ShouldRaiseAlreadyExistsException_WhenUtilisateurExists()
	{
		//arrange
		Utilisateur  utilisateur = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString()))
			.thenReturn(Optional.of(utilisateur));
		
		UtilisateurCreateDto utilisateurDto = new UtilisateurCreateDto();
		utilisateurDto.setEmail("Dummy");
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			utilisateurServiceUnderTest.creer(utilisateurDto);
		}).withMessage("Un utilisateur avec cet email existe déjà");
	}
	
	@Test
	void creer_ShouldRetrurnUtilisateur_WhenSuccess() throws AlreadyExistsException
	{
		//arrange
		Utilisateur  utilisateur = new Utilisateur();
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		UtilisateurCreateDto utilisateurDto = new UtilisateurCreateDto();
		utilisateurDto.setEmail("Dummy");
		utilisateurDto.setPassword("dummyPassword");
		
		Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn("password");
		
		Mockito.when(utilisateurCreateConverterMock.convertDtoToEntity(Mockito.any(UtilisateurCreateDto.class)))
			.thenReturn(utilisateur);
		
		Mockito.when(utilisateurRepositoryMock.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
		
		// act
		Utilisateur actual = utilisateurServiceUnderTest.creer(utilisateurDto);
		
		// assert
		assertThat(actual.getPassword()).isEqualTo("password");
		Mockito.verify(utilisateurRepositoryMock, Mockito.times(1)).save(utilisateur);
	}
	
	@Test
	void obtenirById_ShouldRaiseEntityNotFoundException_WhenUtilisateurNotExists()
	{
		//arrange
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString()))
			.thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			utilisateurServiceUnderTest.obtenir("dummy@domain.tld");
		}).withMessage("L'utilisateur n'existe pas");
	}
	
	@Test
	void obtenirById_ShouldReturnTheme_WhenSuccess() throws EntityNotFoundException
	{
		//arrange
		Utilisateur  utilisateur = new Utilisateur();
		utilisateur.setId(2L);
		Mockito.<Optional<Utilisateur>>when(utilisateurRepositoryMock.findByEmailIgnoreCase(Mockito.anyString()))
			.thenReturn(Optional.of(utilisateur));
		
		// act
		Utilisateur actual = utilisateurServiceUnderTest.obtenir("dummy@domain.tld");
		
		// assert
		assertThat(actual.getId()).isEqualTo(2L);
	}
}
