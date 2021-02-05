package com.ocdev.biblio.apibiblio.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * DTO utilisé pour la consultation d'un ouvrage
 * @author PC_ASUS
 *
 */
@ApiModel(value = "OuvrageConsultDto", description = "Modèle DTO pour la consultation d'un Ouvrage")
public class OuvrageConsultDto implements Serializable
{
	@ApiModelProperty(position = 1, value = "Id de l'ouvrage")
	private Long id;
	@ApiModelProperty(position = 2, value = "Titre de l'ouvrage")
	private String titre;
	@ApiModelProperty(position = 3, value = "Résumé de l'ouvrage")
	private String resume;
	@ApiModelProperty(position = 4, value = "Auteur de l'ouvrage")
	private String auteur;
	@ApiModelProperty(position = 5, value = "Année de parution de l'ouvrage")
	private int anneeEdition;
	@ApiModelProperty(position = 6, required = false, value = "Nombre d'exemplaires disponibles pour prêt")
	private int nbreExemplaire;
	@ApiModelProperty(position = 7, value = "Thème (catégorie) de l'ouvrage")
	private String theme;
	@ApiModelProperty(position = 8, value = "True si réservable")
	private boolean reservable;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public int getNbreExemplaire() {
		return nbreExemplaire;
	}
	public void setNbreExemplaire(int nbreExemplaire) {
		this.nbreExemplaire = nbreExemplaire;
	}
	public String getTheme()
	{
		return theme;
	}
	public void setTheme(String theme)
	{
		this.theme = theme;
	}
	public boolean isReservable() {
		return reservable;
	}
	public void setReservable(boolean reservable) {
		this.reservable = reservable;
	}
}
