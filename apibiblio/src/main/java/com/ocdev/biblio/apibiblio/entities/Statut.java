package com.ocdev.biblio.apibiblio.entities;

import com.ocdev.biblio.apibiblio.utils.Tools;

/**
* Enum pour le statut d'un prêt ou d'une réservation de prêt.
* @author C.Orsini
*
*/
public enum Statut
{
	INCONNU ("Inconnu", false, false),
	RESERVE ("Réservé", false, true),
	DISPONIBLE ("Disponible", false, true),
	EN_COURS ("En cours", true, false),
	PROLONGE ("Prolongé", true, false),
	RETARD ("En retard", true, false),
	RETOURNE ("Retourné", false, false),
	ANNULEE ("Annulée", false, false);
	
	private String libelle;
	private boolean enPret;
	private boolean reserve;
	
	Statut(String libelle, boolean enPret, boolean reserve)
	{
		this.libelle = libelle;
		this.enPret = enPret;
		this.reserve = reserve;
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

	public boolean isReserve() {
		return reserve;
	}

	public void setReserve(boolean reserve) {
		this.reserve = reserve;
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
