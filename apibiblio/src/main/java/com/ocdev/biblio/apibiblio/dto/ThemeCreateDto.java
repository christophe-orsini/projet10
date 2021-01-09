package com.ocdev.biblio.apibiblio.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ThemeCreateDto", description = "Modèle DTO pour la création d'un Theme")
public class ThemeCreateDto implements Serializable
{
	@ApiModelProperty(position = 1, required = true, value = "Nom du thème")
	@NotBlank(message="Le nom du thème est obligatoire")
	private String nom; 
	
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}
}
