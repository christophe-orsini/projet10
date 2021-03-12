package com.ocdev.biblio.apibiblio.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UtilisateurTest
{
	@Test
	void toString_returnsString()
	{
	// 	arrange
		Utilisateur utilisateur = new Utilisateur("dummy@domain.tld", "****", "Nom", "Prenom");
		String expected = "Usager [nom=Nom, prenom=Prenom]";
	
		// act
		String actual = utilisateur.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
