package com.ocdev.biblio.batch.model;

import java.io.Serializable;

/**
 * Classe modèle pour un ouvrage
 * 
 * @author C.Orsini
 *
 */
public class Ouvrage implements Serializable
{
	private Long id;
	private String titre;
	private String resume;
	private String auteur;
	private int anneeEdition;
	private int nbreExemplaire;
	private int nbreExemplaireTotal;
	private Theme theme;
	
	public Ouvrage(){}

	public Ouvrage(String titre, String auteur, int annee, Theme theme)
	{
		super();
		this.titre = titre;
		this.auteur = auteur;
		this.anneeEdition = annee;
		this.theme = theme;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitre()
	{
		return titre;
	}

	public void setTitre(String titre)
	{
		this.titre = titre;
	}

	public String getResume()
	{
		return resume;
	}

	public void setResume(String resume)
	{
		this.resume = resume;
	}

	public String getAuteur()
	{
		return auteur;
	}

	public void setAuteur(String auteur)
	{
		this.auteur = auteur;
	}

	public int getAnneeEdition()
	{
		return anneeEdition;
	}

	public void setAnneeEdition(int anneeEdition)
	{
		this.anneeEdition = anneeEdition;
	}

	public int getNbreExemplaire()
	{
		return nbreExemplaire;
	}

	public void setNbreExemplaire(int nbreExemplaire)
	{
		this.nbreExemplaire = nbreExemplaire;
	}

	
	public int getNbreExemplaireTotal() {
		return nbreExemplaireTotal;
	}

	public void setNbreExemplaireTotal(int nbreExemplaireTotal) {
		this.nbreExemplaireTotal = nbreExemplaireTotal;
	}

	public Theme getTheme()
	{
		return theme;
	}

	public void setTheme(Theme theme)
	{
		this.theme = theme;
	}

	@Override
	public String toString()
	{
		return "Ouvrage [titre=" + titre + ", auteur=" + auteur + "]";
	}
}
