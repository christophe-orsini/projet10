package com.ocdev.biblio.webapp.dto;

import java.util.Date;

import com.ocdev.biblio.webapp.objects.Ouvrage;

public class ReservationDto
{
	private Long id;
	private Date dateHeureReservation;
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
