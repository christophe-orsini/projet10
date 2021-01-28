package com.ocdev.biblio.apibiblio.entities;

import com.ocdev.biblio.apibiblio.utils.Tools;

/**
* Enum pour le statut d'un prêt ou d'une réservation de prêt.
* @author C.Orsini
*
*/
public enum Statut
{
	INCONNU ("Inconnu", false),
	RESERVE ("Réservé", false),
	DISPONIBLE ("Disponible", false),
	EN_COURS ("En cours", true),
	PROLONGE ("Prolongé", true),
	RETARD ("En retard", true),
	RETOURNE ("Retourné", false),
	ANNULEE ("Annulée", false);
	
	private String libelle;
	private boolean enPret;
	
	Statut(String libelle, boolean enPret)
	{
		this.libelle = libelle;
		this.enPret = enPret;
	}

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}
	
	public boolean isEnPret()
	{
		return enPret;
	}

	public void setEnPret(boolean enPret)
	{
		this.enPret = enPret;
	}

	@Override
	public String toString()
	{
		return libelle;
	}
	
	public static Statut convert(String statut)
	{
		if (Tools.stringIsNullOrEmpty(statut)) return INCONNU;
		
		switch(statut)
		{
			case "Réservé": return RESERVE;
			case "Disponible": return DISPONIBLE;
			case "En cours": return EN_COURS;
			case "Prolongé": return PROLONGE;
			case "En retard": return RETARD;
			case "Retourné": return RETOURNE;
			case "Annulée": return ANNULEE;
			default:return INCONNU;
		}
	}
}
