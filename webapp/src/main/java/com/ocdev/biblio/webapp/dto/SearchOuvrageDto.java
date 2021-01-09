package com.ocdev.biblio.webapp.dto;

public class SearchOuvrageDto
{
	private String titre;
	private String auteur;
	private String theme;
	private boolean disponible;
	
	public SearchOuvrageDto()
	{
		super();
		this.titre = "";
		this.auteur = "";
		this.theme = "";
		this.disponible = false;
	}
	
	public SearchOuvrageDto(String titre, String auteur, int anneeEdition, String theme, boolean disponible)
	{
		super();
		this.titre = titre;
		this.auteur = auteur;
		this.theme = theme;
		this.disponible = disponible;
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
	public String getTheme()
	{
		return theme;
	}
	public void setTheme(String theme)
	{
		this.theme = theme;
	}

	public boolean isDisponible()
	{
		return disponible;
	}

	public void setDisponible(boolean disponible)
	{
		this.disponible = disponible;
	}

}
