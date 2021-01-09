package com.ocdev.biblio.webapp.controllers;

import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.ocdev.biblio.webapp.dto.UtilisateurDto;
import com.ocdev.biblio.webapp.errors.BiblioException;
import com.ocdev.biblio.webapp.objects.Utilisateur;
import com.ocdev.biblio.webapp.services.UtilisateurService;

@Controller
public class UtilisateurController
{
	@Autowired	private UtilisateurService utilisateurService;

	@GetMapping(value="/inscription")
	public String inscription(Model model)
	{
		model.addAttribute("utilisateur", new Utilisateur());
		return "utilisateur/creerUtilisateur";
	}		

	@PostMapping(value="/enregistrerUtilisateur")
	public String enregistrerUtilisateur(@ModelAttribute("utilisateur") @Valid UtilisateurDto utilisateur, 
			BindingResult bindingResult, Model model, Principal connectedUser)
	{
		if (bindingResult.hasErrors())
		{
			return "utilisateur/creerUtilisateur";
		}
		
		// enregistrer l'utilisateur
		try
		{
			Utilisateur nouveau = utilisateurService.inscription(utilisateur);
			
			model.addAttribute("login", nouveau.getEmail());
			
			return "utilisateur/messageInscription";
		}
		catch (BiblioException e)
		{
			model.addAttribute("exceptionMessage", e.getMessage());
			
			return "utilisateur/creerUtilisateur";
		}
		catch (Exception e)
		{
			model.addAttribute("exceptionMessage", e.getMessage());
			
			return "utilisateur/creerUtilisateur";
		}
	}
}
