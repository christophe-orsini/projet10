package com.ocdev.biblio.apibiblio.errors;

/**
 * Enumération des status HTTP spécifiques à l'application
 * @author C.Orsini
 *
 */
public enum BiblioHttpStatus
{
	/**
	 * Une entrée existe déjà dans la base de données et elle ne peut pas être dupliquée.
	 */
	BIBLIO_DUPLICATE (460, "Duplicate Entry"),
	/**
	 * Une action ne peut pas être exécutée à cause des règles de gestion.
	 */
	BIBLIO_UNABLE (461, "Unable to do this action"),
	/**
	 * Plus assez d'items dans cette ressource pour pouvoir exécuter cette action.
	 */
	BIBLIO_NOT_ENOUGH (462, "Not enough items for this ressource"),
	/**
	 * Une action n'est pas permise à cause des règles de gestion.
	 */
	BIBLIO_NOT_ALLOWED (469, "Action not allowed");
	
	private int code;
	private String name;
	
	BiblioHttpStatus(int code, String name)
	{
		this.code = code;
		this.name = name;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
