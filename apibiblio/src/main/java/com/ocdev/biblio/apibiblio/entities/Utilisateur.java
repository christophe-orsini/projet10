package com.ocdev.biblio.apibiblio.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe modèle pour un utilisateur
 * @author C.Orsini
 *
 */
@Entity
public class Utilisateur implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String email; // login
	@JsonIgnore
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String nom;
	@Column(nullable = true)
	private String prenom;
	@JsonIgnore
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	@JsonIgnore
	@OneToMany(mappedBy = "abonne", fetch = FetchType.EAGER)
	private Collection<Pret> prets;

	public Utilisateur()
	{
		super();
	}
	
	public Utilisateur(String email, String password, String nom, String prenom)
	{
		super();
		this.email = email;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	public String getPrenom()
	{
		return prenom;
	}
	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}
	public Role getRole()
	{
		return role;
	}
	public void setRole(Role role)
	{
		this.role = role;
	}
	public Collection<Pret> getPrets()
	{
		return prets;
	}
	public void setPrets(Collection<Pret> prets)
	{
		this.prets = prets;
	}

	@Override
	public String toString()
	{
		return "Usager [nom=" + nom + ", prenom=" + prenom + "]";
	}
}
