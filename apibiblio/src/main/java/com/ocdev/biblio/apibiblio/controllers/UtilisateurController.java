package com.ocdev.biblio.apibiblio.controllers;

import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ocdev.biblio.apibiblio.dto.UtilisateurCreateDto;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.services.UtilisateurService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1")
public class UtilisateurController
{
	@Autowired UtilisateurService utilisateurService;
	
	@ApiOperation(value = "Inscription d'un utilisateur", notes = "Inscription d'un nouvel utilisateur de l'application")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "L'utilisateur est correctement inscrit"),
			@ApiResponse(code = 460, message = "Un utilisateur avec le même email existe déjà")
			})
	@PostMapping(value = "/utilisateurs", produces = "application/json")
	public ResponseEntity<Utilisateur> inscrire(@Valid @RequestBody UtilisateurCreateDto utilisateurDto) throws AlreadyExistsException
	{
		Utilisateur result = utilisateurService.creer(utilisateurDto);
		return new ResponseEntity<Utilisateur>(result, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Obtenir un utilisateur, en fonction de son email (login)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "L'utilisateur est retourné dans le corps de la réponse"),
			@ApiResponse(code = 404, message = "L'utilisateur n'existe pas")
			})
	@GetMapping(value = "/utilisateurs/{email}", produces = "application/json")
	public ResponseEntity<Utilisateur> chercheUtilisateur(
			@ApiParam(value="Email (login) de l'utilisateur", required = true, example = "1")
			@PathVariable @Email final String email) throws EntityNotFoundException
	{
		Utilisateur utilisateur = utilisateurService.obtenir(email);
		return new ResponseEntity<Utilisateur>(utilisateur, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Vérifier le login d'un utilisateur", 
			notes = "Vérifier le login d'un utilisateur et obtenir ses droits")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "L'utilisateur est authentifié"),
			@ApiResponse(code = 401, message = "Le login et/ou le mot de passe sont incorrects"),
			@ApiResponse(code = 403, message = "L'URI necessite une Basic Authentication")
			})
	@PostMapping(value = "/checklogin", produces = "application/json")
	public ResponseEntity<String> checkLogin(Principal principal)
	{
		if (principal == null)
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}