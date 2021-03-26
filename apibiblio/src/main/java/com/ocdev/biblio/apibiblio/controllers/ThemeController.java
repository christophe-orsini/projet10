package com.ocdev.biblio.apibiblio.controllers;

import java.net.URI;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.services.ThemeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1")
@Validated
public class ThemeController
{
	@Autowired private ThemeService themeService;
	
	@ApiOperation(value = "Ajout d'un thème", notes = "Ajout d'un nouveau thème")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Le thème est correctement créé"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			@ApiResponse(code = 460, message = "Un thème avec le même nom existe déjà")
			})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value ="/themes", produces = "application/json")
	public ResponseEntity<Theme> ajouterTheme(@Valid @RequestBody final ThemeCreateDto themeCreateDto) throws AlreadyExistsException
	{
		Theme newTheme = themeService.creer(themeCreateDto);
		if (newTheme == null) return ResponseEntity.noContent().build();
		
		//return new ResponseEntity<Theme>(result, HttpStatus.CREATED);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTheme.getId())
                .toUri();

        return ResponseEntity.created(location).build();
	}
	
	@ApiOperation(value = "Liste des thèmes", notes = "Obtenir la liste des thèmes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste des thèmes est retournée dans le corps de la réponse"),
			@ApiResponse(code = 401, message = "Authentification requise")
			})
	@GetMapping(value = "/themes", produces = "application/json")
	public ResponseEntity<Page<Theme>> getThemes(@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "99") int taille) throws EntityNotFoundException
	{
		Pageable paging = PageRequest.of(page,  taille);
		
		Page<Theme> themes = themeService.listeThemes(paging);
		return new ResponseEntity<Page<Theme>>(themes, HttpStatus.OK);
	}
}
