package com.ocdev.biblio.webapp.dto;

import java.util.Date;
import com.ocdev.biblio.webapp.objects.Ouvrage;
import com.ocdev.biblio.webapp.objects.Statut;

public class ReservationDto
{
	private Long id;
	private Date dateHeureReservation;
	private Date dateHeureExpiration;
	private boolean emailEnvoye;
	private Statut statut;
	private int rang;
	private Date dateDisponible;
	private Long abonneId;
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
