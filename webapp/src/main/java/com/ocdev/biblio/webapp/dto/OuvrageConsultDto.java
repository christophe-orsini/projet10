package com.ocdev.biblio.webapp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO utilisé pour la consultation d'un ouvrage
 * @author PC_ASUS
 *
 */
public class OuvrageConsultDto implements Serializable
{
	private Long id;
	private String titre;
	private String resume;
	private String auteur;
	private int anneeEdition;
	private int nbreExemplaire;
	private String theme;
	private boolean reservable;
	private Date prochainRetour;
	private int nbreReservations;
	
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
	public Date getProchainRetour() {
		return prochainRetour;
	}
	public void setProchainRetour(Date prochainRetour) {
		this.prochainRetour = prochainRetour;
	}
	public int getNbreReservations() {
		return nbreReservations;
	}
	public void setNbreReservations(int nbreReservations) {
		this.nbreReservations = nbreReservations;
	}

}
