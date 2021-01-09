package com.ocdev.biblio.batch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.ocdev.biblio.batch.model.Utilisateur;

/**
 * Classe de création de contenus d'emails
 * @author C.Orsini
 *
 */
@Service
public class EmailContentBuilder
{
	@Autowired PropertiesConfigurationService properties;
	@Autowired TemplateEngine templateEngine;
	
	/**
	 * Création du contenu d'un email de relance pour retard
	 * 
	 * Le contenu est créé par le templateEngine
	 * @param abonne L'abonné avec la liste de ses prêts en retard
	 * @return Le texte à mettre dans le contenu de l'email
	 */
	public String buildDelayEmail(Utilisateur abonne)
	{
		Context context = new Context();
        context.setVariable("societe", properties.societe());
        context.setVariable("abonne", abonne);
        context.setVariable("email", properties.emailContact());
        context.setVariable("notreSite", properties.urlWebApp());
        
        return templateEngine.process("email", context);
	}
}
