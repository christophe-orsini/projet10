package com.ocdev.biblio.batch.model;

/**
 * Enum pour les rôles.
 * Utilisé par Spring Security.
 * @author C.Orsini
 *
 */
public enum Role
{
	ROLE_ABONNE("Abonné"), 
	ROLE_EMPLOYE("Employé"), 
	ROLE_ADMINISTRATEUR("Administrateur");

	private String name;

	Role(String name)
	{
		this.name = name;
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
