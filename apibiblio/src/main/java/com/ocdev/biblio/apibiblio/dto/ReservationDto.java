package com.ocdev.biblio.apibiblio.dto;

import java.util.Date;
import javax.validation.constraints.Min;

import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Statut;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ReservationDto", description = "Modèle DTO pour la classe Pret")
public class ReservationDto
{
	@ApiModelProperty(position = 1, required = false, value = "ID de la réservation", allowEmptyValue = true)
	private Long id;
	@ApiModelProperty(position = 2, required = false, value = "Date de réservation", example = "10/07/2020 11:53")
	private Date dateHeureReservation;
	@ApiModelProperty(position = 3, required = false, value = "Date d'expiration d'une réservation disponible", example = "10/07/2020 11:53")
	private Date dateHeureExpiration;
	@ApiModelProperty(position = 4, required = false, value = "Drapeau pour email envoyé si réservation disponible", example = "true")
	private boolean emailEnvoye;
	@ApiModelProperty(position = 5, required = false, value = "Statut de la réservation", example = "DISPONIBLE")
	private Statut statut;
	@ApiModelProperty(position = 6, required = false, value = "Rang de l'abonné dans la liste d'attente", example = "1")
	@Min(1)
	private int rang;
	@ApiModelProperty(position = 7, required = false, value = "Date de disponibilité prévue", example = "14/08/2020")
	private Date dateDisponible;
	@ApiModelProperty(position = 8, required = false, value = "L'ID de l'abonné ayant réservé l'ouvrage", example = "1")
	@Min(1)
	private Long abonneId;
	@ApiModelProperty(position = 9, required = false, value = "L'ouvrage réservé", example = "1")
	private Ouvrage ouvrage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateHeureReservation() {
		return dateHeureReservation;
	}
	public void setDateHeureReservation(Date dateHeureReservation) {
		this.dateHeureReservation = dateHeureReservation;
	}
	public Date getDateHeureExpiration() {
		return dateHeureExpiration;
	}
	public void setDateHeureExpiration(Date dateHeureExpiration) {
		this.dateHeureExpiration = dateHeureExpiration;
	}
	public boolean isEmailEnvoye() {
		return emailEnvoye;
	}
	public void setEmailEnvoye(boolean emailEnvoye) {
		this.emailEnvoye = emailEnvoye;
	}
	public Statut getStatut() {
		return statut;
	}
	public void setStatut(Statut statut) {
		this.statut = statut;
	}
	public int getRang() {
		return rang;
	}
	public void setRang(int rang) {
		this.rang = rang;
	}
	public Date getDateDisponible() {
		return dateDisponible;
	}
	public void setDateDisponible(Date dateDisponible) {
		this.dateDisponible = dateDisponible;
	}
	public Long getAbonneId()
	{
		return abonneId;
	}
	public void setAbonneId(Long abonneId)
	{
		this.abonneId = abonneId;
	}
	public Ouvrage getOuvrage() {
		return ouvrage;
	}
	public void setOuvrage(Ouvrage ouvrage) {
		this.ouvrage = ouvrage;
	}
}
