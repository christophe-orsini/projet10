package com.ocdev.biblio.apibiblio.criterias;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.utils.Tools;

public class OuvrageSpecification implements Specification<Ouvrage>
{
	private OuvrageCriteria ouvrageCriterie;
	
	public OuvrageSpecification(OuvrageCriteria filter)
	{
		super();
		this.ouvrageCriterie = filter;
	}

	@Override
	public Predicate toPredicate(Root<Ouvrage> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
	{
		Predicate predicate = criteriaBuilder.conjunction();
		
		if (!Tools.stringIsNullOrEmpty(ouvrageCriterie.getTitre()))
		{
			predicate.getExpressions().add(criteriaBuilder.like(root.get("titre"), "%" + ouvrageCriterie.getTitre() + "%"));
		}
		if (!Tools.stringIsNullOrEmpty(ouvrageCriterie.getAuteur()))
		{
			predicate.getExpressions().add(criteriaBuilder.like(root.get("auteur"), "%" + ouvrageCriterie.getAuteur() + "%"));
		}
		if (ouvrageCriterie.getAnneeEdition() > 0)
		{
			predicate.getExpressions().add(criteriaBuilder.ge(root.get("anneeEdition"), ouvrageCriterie.getAnneeEdition()));
		}
		if (ouvrageCriterie.getNbreExemplaire() > 0)
		{
			predicate.getExpressions().add(criteriaBuilder.ge(root.get("nbreExemplaire"), ouvrageCriterie.getNbreExemplaire()));
		}
		
		if (!Tools.stringIsNullOrEmpty(ouvrageCriterie.getTheme()))
		{
			predicate.getExpressions().add(criteriaBuilder.like(root.join("theme").get("nom"), "%" + ouvrageCriterie.getTheme() + "%"));
		}
		
		return criteriaBuilder.and(predicate);
	}
}
