package com.ocdev.biblio.apibiblio.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PretTest
{
	@Test
	void toString_returnsString()
	{
	// 	arrange
		Pret pret = new Pret();
		pret.setStatut(Statut.EN_COURS);
		String expected = "Pret [dateDebut=null, dateFinPrevu=null, statut=En cours, abonne=null, ouvrage=null]";
	
		// act
		String actual = pret.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
