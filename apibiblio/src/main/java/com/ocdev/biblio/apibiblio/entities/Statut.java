package com.ocdev.biblio.apibiblio.entities;

import com.ocdev.biblio.apibiblio.utils.Tools;

/**
* Enum pour le statut d'un prêt.
* @author C.Orsini
*
*/
public enum Statut
{
	INCONNU ("Inconnu"),
	RESERVE ("Réservé"),
	DISPONIBLE ("Disponible"),
	EN_COURS ("En cours"),
	PROLONGE ("Prolongé"),
	RETARD ("En retard"),
	RETOURNE ("Retourné"),
	ANNULEE ("Annulée");
	
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
