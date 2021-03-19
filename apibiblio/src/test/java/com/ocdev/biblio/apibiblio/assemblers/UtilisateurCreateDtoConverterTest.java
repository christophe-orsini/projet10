package com.ocdev.biblio.apibiblio.assemblers;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ocdev.biblio.apibiblio.dto.UtilisateurCreateDto;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;

class UtilisateurCreateDtoConverterTest 
{
	private IDtoConverter<Utilisateur, UtilisateurCreateDto> converterUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		converterUnderTest = new UtilisateurCreateDtoConverter();
	}
	
	@Test
	void convertDtoToEntity_returneEmail_whenSuccess()
	{
	// 	arrange
		UtilisateurCreateDto dto = new UtilisateurCreateDto();
		dto.setNom("Nom");
		
		// act
		Utilisateur entity = converterUnderTest.convertDtoToEntity(dto);
		
		// assert
		assertThat(entity.getNom()).isEqualTo("Nom");
	}
	
	@Test
	void convertEntityToDto_raiseNotYetImplementedException()
	{
		// 	arrange
		Utilisateur entity = new Utilisateur();
	
		// act & assert
		assertThatExceptionOfType(NotYetImplementedException.class).isThrownBy(() ->
		{
			converterUnderTest.convertEntityToDto(entity);
		}).withMessage("Not yet implemented!");
	}
}
