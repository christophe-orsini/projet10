package com.ocdev.biblio.apibiblio.errors;

/**
 * Exception levée si une prolongation de pêt est impossible.
 * Levé lorsque une prolongation de prêt depasse la durée maximale.
 * Http Status Code : {@link com.ocdev.biblio.apibiblio.errors.BiblioHttpStatus#BIBLIO_UNABLE}
 * @author C.Orsini
 */
public class DelayLoanException extends BiblioException
{
	public DelayLoanException(String message)
	{
		super(message);
	}
}
