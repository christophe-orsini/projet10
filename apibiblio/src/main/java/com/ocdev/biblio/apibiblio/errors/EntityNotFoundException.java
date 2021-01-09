package com.ocdev.biblio.apibiblio.errors;

/**
 * Classe exception pour une entité non trouvée et obligatoire.
 * Levé lorsque une recherche n'aboutit pas et qu'elle devrait.
 * @author C.Orsini
 */
public class EntityNotFoundException extends BiblioException
{
	public EntityNotFoundException(String message)
	{
		super(message);
	}
}
