package com.ocdev.biblio.apibiblio.assemblers;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Theme;

class OuvrageConsultDtoConverterTest 
{
	private IDtoConverter<Ouvrage, OuvrageConsultDto> converterUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		converterUnderTest = new OuvrageConsultDtoConverter();
	}
	
	@Test
	void convertDtoToEntity_raiseNotYetImplementedException()
	{
		// 	arrange
		OuvrageConsultDto dto = new OuvrageConsultDto();
	
		// act & assert
		assertThatExceptionOfType(NotYetImplementedException.class).isThrownBy(() ->
		{
			converterUnderTest.convertDtoToEntity(dto);
		}).withMessage("Not yet implemented!");
	}
	
	@Test
	void convertEntityToDto_returnTheme_whenSuccess()
	{
	// 	arrange
		Ouvrage entity = new Ouvrage();
		entity.setTheme(new Theme("Theme"));
		
		// act
		OuvrageConsultDto dto = converterUnderTest.convertEntityToDto(entity);
		
		// assert
		assertThat(dto.getTheme()).isEqualTo("Theme");
	}
}
