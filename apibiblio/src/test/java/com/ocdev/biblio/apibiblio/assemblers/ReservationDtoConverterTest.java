package com.ocdev.biblio.apibiblio.assemblers;

import static org.assertj.core.api.Assertions.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;

class ReservationDtoConverterTest 
{
	private IDtoConverter<Pret, ReservationDto> converterUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		converterUnderTest = new ReservationDtoConverter();
	}
	
	@Test
	void convertDtoToEntity_raiseNotYetImplementedException()
	{
		// 	arrange
		ReservationDto dto = new ReservationDto();
	
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
		Pret entity = new Pret();
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setId(99L);
		entity.setOuvrage(ouvrage);
		Utilisateur abonne = new Utilisateur();
		abonne.setId(88L);
		entity.setAbonne(abonne);
		
		// act
		ReservationDto dto = converterUnderTest.convertEntityToDto(entity);
		
		// assert
		assertThat(dto.getOuvrage().getId()).isEqualTo(99L);
		assertThat(dto.getAbonneId()).isEqualTo(88L);
	}
}
