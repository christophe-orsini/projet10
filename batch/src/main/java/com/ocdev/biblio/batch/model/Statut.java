package com.ocdev.biblio.batch.model;

/**
* Enum pour le statut d'un prêt.
* @author C.Orsini
*
*/
public enum Statut
{
	INCONNU ("Inconnu"),
	RESERVE ("Réservé"),
	EN_COURS ("En cours"),
	PROLONGE ("Prolongé"),
	RETARD ("En retard"),
	RETOURNE ("Retourné");
	
	private String libelle;
	
	Statut(String libelle)
	{
		this.libelle = libelle;
	}

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}
	
	@Override
	public String toString()
	{
		return libelle;
	}
	
	public static Statut convert(String statut)
	{
		if (statut == null || statut.contentEquals("")) return INCONNU;
		
		switch(statut)
		{
			case "Réservé": return RESERVE;
			case "En cours": return EN_COURS;
			case "Prolongé": return PROLONGE;
			case "En retard": return RETARD;
			case "Retourné": return RETOURNE;
			default:return INCONNU;
		}
	}
}
