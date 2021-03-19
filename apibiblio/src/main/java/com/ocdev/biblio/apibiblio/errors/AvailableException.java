package com.ocdev.biblio.apibiblio.errors;

/**
 * Classe exception pour un ouvrage disponible.
 * Levé lorsque une demande de réservation d'un ouvrage ne peut pas être satisfaite car l'ouvrage est disponible.
 * Http Status Code : {@link com.ocdev.biblio.apibiblio.errors.BiblioHttpStatus#BIBLIO_AVAILABLE}
 * @author C.Orsini
 */
public class AvailableException extends BiblioException
{
	public AvailableException(String message)
	{
		super(message);
	}
}
