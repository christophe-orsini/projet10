package com.ocdev.biblio.apibiblio.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "OuvrageCreateDto", description = "Modèle DTO pour la création d'un Ouvrage")
public class OuvrageCreateDto implements Serializable
{
	@ApiModelProperty(position = 1, required = true, value = "Titre de l'ouvrage")
	@NotBlank(message="Le titre est obligatoire")
	private String titre;
	@ApiModelProperty(position = 2, required = false, value = "Résumé de l'ouvrage")
	private String resume;
	@ApiModelProperty(position = 3, required = true, value = "Auteur de l'ouvrage")
	@NotBlank(message="L'auteur est obligatoire")
	private String auteur;
	@ApiModelProperty(position = 4, required = true, value = "Année de parution de l'ouvrage")
	@Positive(message="L'année de parution est obligatoire")
	private int anneeEdition;
	@ApiModelProperty(position = 5, required = false, value = "Nombre d'exemplaires total disponibles pour prêt")
	@PositiveOrZero(message = "Le nombre d'exemplaires ne peut pas être négatif")
	private int nbreExemplaireTotal;
	@ApiModelProperty(position = 6, required = true, value = "Thème (catégorie) de l'ouvrage")
	@Positive(message="Le thème est obligatoire")
	private Long theme;

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
	public int getNbreExemplaireTotal()
	{
		return nbreExemplaireTotal;
	}
	public void setNbreExemplaireTotal(int nbreExemplaireTotal)
	{
		this.nbreExemplaireTotal = nbreExemplaireTotal;
	}
	public Long getTheme()
	{
		return theme;
	}
	public void setTheme(Long theme)
	{
		this.theme = theme;
	}
}
