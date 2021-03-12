package com.ocdev.biblio.apibiblio.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OuvrageTest
{
	@Test
	void toString_returnsString()
	{
	// 	arrange
		Ouvrage ouvrage = new Ouvrage();
		ouvrage.setTitre("Titre");
		ouvrage.setAuteur("Auteur");
		String expected = "Ouvrage [titre=Titre, auteur=Auteur]";
	
		// act
		String actual = ouvrage.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
