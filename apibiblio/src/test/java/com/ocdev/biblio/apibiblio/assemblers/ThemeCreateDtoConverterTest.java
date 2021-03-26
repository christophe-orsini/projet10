package com.ocdev.biblio.apibiblio.assemblers;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;

class ThemeCreateDtoConverterTest 
{
	private IDtoConverter<Theme, ThemeCreateDto> converterUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		converterUnderTest = new ThemeCreateDtoConverter();
	}
	
	@Test
	void convertDtoToEntity_returneTheme_whenSuccess()
	{
	// 	arrange
		ThemeCreateDto dto = new ThemeCreateDto();
		dto.setNom("Theme");
		
		// act
		Theme entity = converterUnderTest.convertDtoToEntity(dto);
		
		// assert
		assertThat(entity.getNom()).isEqualTo("Theme");
	}
	
	@Test
	void convertEntityToDto_raiseNotYetImplementedException()
	{
		// 	arrange
		Theme entity = new Theme();
	
		// act & assert
		assertThatExceptionOfType(NotYetImplementedException.class).isThrownBy(() ->
		{
			converterUnderTest.convertEntityToDto(entity);
		}).withMessage("Not yet implemented!");
	}
}
