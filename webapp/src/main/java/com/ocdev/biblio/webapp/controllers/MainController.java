package com.ocdev.biblio.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController
{
	@GetMapping({"/", "/index", "/login", "/logout"})
	public String login(Model model)
	{	
		return "/login";
	}
}
