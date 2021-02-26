package com.ocdev.biblio.apibiblio.utils;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ToolsTest
{
	@Test
	void stringIsNullOrEmpty_withNull_returnsTrue()
	{
		// arrange
		String expected = null;
		
		// act
		boolean actual = Tools.stringIsNullOrEmpty(expected);
		
		// assert
		assertThat(actual).isTrue();
	}

	@Test
	void stringIsNullOrEmpty_withEmpty_returnsTrue()
	{
		// arrange
		String expected = "";
		
		// act
		boolean actual = Tools.stringIsNullOrEmpty(expected);
		
		// assert
		assertThat(actual).isTrue();
	}
	
	@Test
	void stringIsNullOrEmpty_withString_returnsFalse()
	{
		// arrange
		String expected = "dummy";
		
		// act
		boolean actual = Tools.stringIsNullOrEmpty(expected);
		
		// assert
		assertThat(actual).isFalse();
	}
	
	@Test
	void stringIsNullOrEmpty_withBlankString_returnsTrue()
	{
		// arrange
		String expected = "          ";
		
		// act
		boolean actual = Tools.stringIsNullOrEmpty(expected);
		
		// assert
		assertThat(actual).isTrue();
	}
}
