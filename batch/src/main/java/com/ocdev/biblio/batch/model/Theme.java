package com.ocdev.biblio.batch.model;

import java.io.Serializable;

/**
 * Classe modèle pour un thème
 * @author C.Orsini
 *
 */
public class Theme implements Serializable
{
	private Long id;

	private String nom;

	public Theme()
	{
		super();
	}

	public Theme(String nom)
	{
		super();
		this.nom = nom;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	@Override
	public String toString()
	{
		return nom;
	}
}
