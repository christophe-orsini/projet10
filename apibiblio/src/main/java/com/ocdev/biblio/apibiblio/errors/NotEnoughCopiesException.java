package com.ocdev.biblio.apibiblio.errors;

/**
 * Classe exception pour un nombre d'exemplaires d'ouvrage insufisant.
 * Levé lorsque une demande de prêt d'un ouvrage ne peut pas être satisfaite à cause du manqiue d'exemplaires.
 * Http Status Code : {@link com.ocdev.biblio.apibiblio.errors.BiblioHttpStatus#BIBLIO_NOT_ENOUGH}
 * @author C.Orsini
 */
public class NotEnoughCopiesException extends BiblioException
{
	public NotEnoughCopiesException(String message)
	{
		super(message);
	}
}
