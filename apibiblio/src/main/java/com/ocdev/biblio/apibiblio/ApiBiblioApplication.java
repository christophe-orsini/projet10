package com.ocdev.biblio.apibiblio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Classe principale de l'application.
 * Cette classe s'appuie sur le framework Spring Boot.
 * @author C.Orsini
 *
 */
@SpringBootApplication
public class ApiBiblioApplication
{
	/**
	 * Méthode <b>Main</b>.
	 * Les arguments ne sont pas utilisés dans cette application.
	 * @param args nil.
	 * @throws Exception si une exception non gérée remonte jusqu'à la méthode.
	 */

	public static void main(String[] args) throws Exception
	{
		SpringApplication.run(ApiBiblioApplication.class, args);
	}
}
