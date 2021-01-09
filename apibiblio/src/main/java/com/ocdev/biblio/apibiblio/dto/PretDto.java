package com.ocdev.biblio.apibiblio.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PretDto", description = "Modèle DTO pour la classe Pret")
public class PretDto
{
	@ApiModelProperty(position = 1, required = false, value = "ID du pret attribuée par le système", allowEmptyValue = true)
	private Long id;
	@ApiModelProperty(position = 2, required = true, value = "Date de début du prêt", example = "10/07/2020")
	@NotNull(message = "La date de début doit être renseignée")
	private Date dateDebut;
	@ApiModelProperty(position = 3, required = false, value = "Date de fin prévue du prêt (normalement date de début + 4 semaines)", example = "14/08/2020")
	private Date dateFinPrevu;
	@ApiModelProperty(position = 4, required = false, value = "Date effective de retour du prêt", example = "02/08/2020")
	private Date dateRetour;
	@ApiModelProperty(position = 5, required = false, value = "Le statut du prêt (Inconnu, Réservé, En cours, Prolongé, En retard, retourné", example = "En cours")
	private String statut;
	@ApiModelProperty(position = 6, required = true, value = "L'ID de l'abonné emprunter de l'ouvrage", example = "1")
	@NotNull(message = "L'id de l'abonné est obligatoire")
	@Min(1)
	private Long abonneId;
	@ApiModelProperty(position = 7, required = true, value = "L'ID de l'ouvrage emprunté", example = "1")
	@NotNull(message = "L'id de l'ouvrage est obligatoire")
	@Min(1)
	private Long ouvrageId;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Date getDateDebut()
	{
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}
	public Date getDateFinPrevu()
	{
		return dateFinPrevu;
	}
	public void setDateFinPrevu(Date dateFinPrevu)
	{
		this.dateFinPrevu = dateFinPrevu;
	}
	public Date getDateRetour()
	{
		return dateRetour;
	}
	public void setDateRetour(Date dateRetour)
	{
		this.dateRetour = dateRetour;
	}
	public String getStatut()
	{
		return statut;
	}
	public void setStatut(String statut)
	{
		this.statut = statut;
	}
	public Long getAbonneId()
	{
		return abonneId;
	}
	public void setAbonneId(Long abonneId)
	{
		this.abonneId = abonneId;
	}
	public Long getOuvrageId()
	{
		return ouvrageId;
	}
	public void setOuvrageId(Long ouvrageId)
	{
		this.ouvrageId = ouvrageId;
	}
}
