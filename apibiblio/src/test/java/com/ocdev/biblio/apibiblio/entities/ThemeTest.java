package com.ocdev.biblio.apibiblio.entities;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ThemeTest
{
	@Test
	void toString_returnsString()
	{
	// 	arrange
		Theme theme = new Theme("Policier");
		
		String expected = "Policier";
	
		// act
		String actual = theme.toString();
		
		// assert
		assertThat(actual).isEqualTo(expected);
	}
}
