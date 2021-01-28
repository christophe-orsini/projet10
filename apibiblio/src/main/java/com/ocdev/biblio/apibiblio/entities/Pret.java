package com.ocdev.biblio.apibiblio.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe modèle pour un prêt
 * @author C.Orsini
 *
 */
@Entity
public class Pret implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateHeureReservation;
	
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateHeureExpiration;
	
	@Column(nullable = true)
	private boolean emailEnvoye;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dateDebut;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dateFinPrevu;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dateRetour;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Statut statut;

	@Column(nullable = true)
	private int periodes;

	@Column(nullable = true)
	private int prolongationsPossible;
	
	@JoinColumn(name = "abonne_id")
	@ManyToOne
	private Utilisateur abonne;

	@JoinColumn(name = "ouvrage_id")
	@ManyToOne
	private Ouvrage ouvrage;

	public Pret()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Pret(Utilisateur abonne, Ouvrage ouvrage)
	{
		super();
		this.abonne = abonne;
		this.ouvrage = ouvrage;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
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

	public Statut getStatut()
	{
		return statut;
	}

	public void setStatut(Statut statut)
	{
		this.statut = statut;
	}

	public int getPeriodes()
	{
		return periodes;
	}

	public void setPeriodes(int periodes)
	{
		this.periodes = periodes;
	}
	
	public int getProlongationsPossible()
	{
		return prolongationsPossible;
	}

	public void setProlongationsPossible(int nbreProlongations)
	{
		this.prolongationsPossible = nbreProlongations;
	}

	public Utilisateur getAbonne()
	{
		return abonne;
	}

	public void setAbonne(Utilisateur abonne)
	{
		this.abonne = abonne;
	}

	public Ouvrage getOuvrage()
	{
		return ouvrage;
	}

	public void setOuvrage(Ouvrage ouvrage)
	{
		this.ouvrage = ouvrage;
	}

	@Override
	public String toString()
	{
		return "Pret [dateDebut=" + dateDebut + ", dateFinPrevu=" + dateFinPrevu + ", statut=" + statut + ", abonne="
				+ abonne + ", ouvrage=" + ouvrage + "]";
	}
}
