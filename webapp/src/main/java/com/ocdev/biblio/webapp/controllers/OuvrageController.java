package com.ocdev.biblio.webapp.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ocdev.biblio.webapp.dto.SearchOuvrageDto;
import com.ocdev.biblio.webapp.objects.Ouvrage;
import com.ocdev.biblio.webapp.services.OuvrageService;

@Controller
public class OuvrageController
{
	@Autowired OuvrageService ouvrageService;
	
	@GetMapping("/abonne/listeOuvrages")
	public String listeOuvrages(
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "99") int taille, 
			Model model, Principal utilisateur)
	{
		Page<Ouvrage> response = ouvrageService.listeOuvrages(new SearchOuvrageDto(), page, taille);
		model.addAttribute("ouvrages", response.getContent());
		
		model.addAttribute("ouvrageCherche", new SearchOuvrageDto());
		
		return "/ouvrage/listeOuvrages";
	}
	
	@GetMapping("/abonne/detailOuvrage/{id}")
	public String consulter(@PathVariable Long id,  Model model, Principal utilisateur)
	{
		Ouvrage response = ouvrageService.getOuvrageById(id);
		model.addAttribute("ouvrage", response);
		return "/ouvrage/detailOuvrage";
	}
	
	@PostMapping("/abonne/rechercheOuvrages")
	public String rechercherOuvrage(@ModelAttribute("ouvrageCherche") SearchOuvrageDto ouvrageCherche,
			Model model, Principal utilisateur)
	{
		Page<Ouvrage> response = ouvrageService.listeOuvrages(ouvrageCherche, 0, 10);
		model.addAttribute("ouvrages", response.getContent());
		
		model.addAttribute("ouvrageCherche", ouvrageCherche);
		
		return "/ouvrage/listeOuvrages";
	}
}
