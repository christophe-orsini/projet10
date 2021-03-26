package com.ocdev.biblio.apibiblio.assemblers;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;

class OuvrageCreateDtoConverterTest 
{
	private IDtoConverter<Ouvrage, OuvrageCreateDto> converterUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		converterUnderTest = new OuvrageCreateDtoConverter();
	}
	
	@Test
	void convertDtoToEntity_returne99L_whenSuccess()
	{
	// 	arrange
		OuvrageCreateDto dto = new OuvrageCreateDto();
		dto.setTheme(99L);
		
		// act
		Ouvrage entity = converterUnderTest.convertDtoToEntity(dto);
		
		// assert
		assertThat(entity.getTheme().getId()).isEqualTo(99L);
	}
	
	@Test
	void convertEntityToDto_raiseNotYetImplementedException()
	{
		// 	arrange
		Ouvrage entity = new Ouvrage();
	
		// act & assert
		assertThatExceptionOfType(NotYetImplementedException.class).isThrownBy(() ->
		{
			converterUnderTest.convertEntityToDto(entity);
		}).withMessage("Not yet implemented!");
	}
}
