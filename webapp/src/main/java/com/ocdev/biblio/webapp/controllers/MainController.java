package com.ocdev.biblio.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ocdev.biblio.webapp.utils.AppSettings;

@Controller
public class MainController
{
	@GetMapping({"/", "/index", "/login", "/logout"})
	public String login(Model model)
	{	
		if (AppSettings.getSetting("version").length() > 0)
		{
			model.addAttribute("version", "Version : " + AppSettings.getSetting("version"));
		}
		return "/login";
	}
}
