package com.ocdev.biblio.apibiblio.errors;

/**
 * Exception levée si une action non permise est demandée.
* Http Status Code : {@link com.ocdev.biblio.apibiblio.errors.BiblioHttpStatus#BIBLIO_NOT_ALLOWED}
 * @author C.Orsini
 */
public class NotAllowedException extends BiblioException
{
	public NotAllowedException(String message)
	{
		super(message);
	}
}
