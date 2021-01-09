package com.ocdev.biblio.webapp.objects;

import java.io.Serializable;
import java.util.Collection;

/**
 * Classe modèle pour un utilisateur
 * @author C.Orsini
 *
 */
public class Utilisateur implements Serializable
{
	private Long id;
	private String email; // login
	private String password;
	private String nom;
	private String prenom;
	private Role role;
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
