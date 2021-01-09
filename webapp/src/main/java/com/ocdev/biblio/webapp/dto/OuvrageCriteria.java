package com.ocdev.biblio.webapp.dto;

public class OuvrageCriteria
{
	private String titre;
	private String auteur;
	private int anneeEdition;
	private String theme;
	private int nbreExemplaire;
	
	public OuvrageCriteria()
	{
		super();
		this.titre = "";
		this.auteur = "";
		this.anneeEdition = 0;
		this.theme = "";
		this.nbreExemplaire = 0;
	}
	
	public OuvrageCriteria(String titre, String auteur, int anneeEdition, String theme, int nbreExemplaire)
	{
		super();
		this.titre = titre;
		this.auteur = auteur;
		this.anneeEdition = anneeEdition;
		this.theme = theme;
		this.nbreExemplaire = nbreExemplaire;
	}
	
	public String getTitre()
	{
		return titre;
	}
	public void setTitre(String titre)
	{
		this.titre = titre;
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
	public String getTheme()
	{
		return theme;
	}
	public void setTheme(String theme)
	{
		this.theme = theme;
	}
	public int getNbreExemplaire()
	{
		return nbreExemplaire;
	}
	public void setNbreExemplaire(int nbreExemplaire)
	{
		this.nbreExemplaire = nbreExemplaire;
	}
}
