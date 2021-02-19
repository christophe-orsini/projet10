package com.ocdev.biblio.webapp.controllers;

import java.security.Principal;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ocdev.biblio.webapp.objects.Pret;
import com.ocdev.biblio.webapp.services.PretService;

@Controller
public class PretController
{
	@Autowired PretService pretService;
	
	@GetMapping("/abonne/listePrets")
	public String consulter(Model model, Principal utilisateur)
	{
		Page<Pret> response = pretService.listePrets(utilisateur, 0, 10);
		model.addAttribute("prets", response.getContent());
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		model.addAttribute("today", today.getTime());
		
		return "/pret/listePrets";
	}
	
	@GetMapping("/abonne/prolongerPret/{id}")
	public String prolonger(@PathVariable Long id, Model model, Principal utilisateur)
	{
		pretService.prolonger(utilisateur, id);
				
		return "redirect:/abonne/listePrets";
	}
}
