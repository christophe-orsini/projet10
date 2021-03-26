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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.dao.ThemeRepository;
import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;


@ExtendWith(MockitoExtension.class)
class ThemeServiceImplTest
{
	private ThemeServiceImpl themeServiceUnderTest;
	
	@Mock private ThemeRepository themeRepositoryMock;
	@Mock private IDtoConverter<Theme, ThemeCreateDto> themeCreateConverterMock;
	@Mock Page<Theme> pageMock;
	
	@BeforeEach
	void setUp() throws Exception
	{
		 MockitoAnnotations.initMocks(this);
		 themeServiceUnderTest = new ThemeServiceImpl(
				themeRepositoryMock,
				themeCreateConverterMock);
	}
	
	@Test
	void creer_ShouldRaiseAlreadyExistsException_WhenThemeExists()
	{
		//arrange
		Theme  theme = new Theme();
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findByNomIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(theme));
		
		ThemeCreateDto themeDto = new ThemeCreateDto();
		themeDto.setNom("Dummy");
		
		// act & assert
		assertThatExceptionOfType(AlreadyExistsException.class).isThrownBy(() ->
		{
			themeServiceUnderTest.creer(themeDto);
		}).withMessage("Un thème avec le même nom existe déjà");
	}
	
	@Test
	void creer_ShouldReturnNewTheme_WhenSuccess() throws AlreadyExistsException
	{
		//arrange
		Theme  theme = new Theme();
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findByNomIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		ThemeCreateDto themeDto = new ThemeCreateDto();
		themeDto.setNom("Dummy");
		
		Mockito.when(themeCreateConverterMock.convertDtoToEntity(Mockito.any(ThemeCreateDto.class))).thenReturn(theme);
		
		Mockito.when(themeRepositoryMock.save(Mockito.any(Theme.class))).thenReturn(theme);

		// act
		Theme actual = themeServiceUnderTest.creer(themeDto);
		
		// assert
		assertThat(actual).isEqualTo(theme);
		Mockito.verify(themeRepositoryMock, Mockito.times(1)).save(theme);
	}
	
	@Test
	void obtenirById_ShouldRaiseEntityNotFoundException_WhenThemeNotExists()
	{
		//arrange
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			themeServiceUnderTest.obtenir(3L);
		}).withMessage("Le thème n'existe pas");
	}
	
	@Test
	void obtenirById_ShouldReturnTheme_WhenSuccess() throws EntityNotFoundException
	{
		//arrange
		Theme theme = new Theme();
		theme.setId(3L);
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(theme));
		
		// act
		Theme actual = themeServiceUnderTest.obtenir(3L);
		
		// assert
		assertThat(actual.getId()).isEqualTo(3L);
	}
	
	@Test
	void obtenirByNom_ShouldRaiseEntityNotFoundException_WhenThemeNotExists()
	{
		//arrange
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findByNomIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
		
		// act & assert
		assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
		{
			themeServiceUnderTest.obtenir("Dummy");
		}).withMessage("Le thème n'existe pas");
	}
	
	@Test
	void obtenirByNom_ShouldReturnTheme_WhenSuccess() throws EntityNotFoundException
	{
		//arrange
		Theme theme = new Theme();
		theme.setNom("Dummy");
		Mockito.<Optional<Theme>>when(themeRepositoryMock.findByNomIgnoreCase(Mockito.anyString())).thenReturn(Optional.of(theme));
		
		// act
		Theme actual = themeServiceUnderTest.obtenir("Dummy");
		
		// assert
		assertThat(actual.getNom()).isEqualTo("Dummy");
	}
	
	@Test
	void listeThemes_ShouldReturnEmpty_WhenNoThemes()
	{
		//arrange
		Pageable pageable = PageRequest.of(0, 8);
		
		Mockito.when(themeRepositoryMock.findAll(pageable)).thenReturn(pageMock);
		
		// act 
		themeServiceUnderTest.listeThemes(pageable);
		
		// assert
		Mockito.verify(themeRepositoryMock, Mockito.times(1)).findAll(pageable);
	}
}
