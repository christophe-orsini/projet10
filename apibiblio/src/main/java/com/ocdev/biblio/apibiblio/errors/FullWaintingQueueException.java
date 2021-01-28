package com.ocdev.biblio.apibiblio.errors;

/**
 * Classe exception pour une file d'attente de réservation pleine.
 * Levé lorsque une demande de réservation d'un ouvrage ne peut pas être satisfaite à cause d'une file d'attente pleine.
 * Http Status Code : {@link com.ocdev.biblio.apibiblio.errors.BiblioHttpStatus#BIBLIO_NO_MORE}
 * @author C.Orsini
 */
public class FullWaintingQueueException extends BiblioException
{
	public FullWaintingQueueException(String message)
	{
		super(message);
	}
}
