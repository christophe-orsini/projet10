package com.ocdev.biblio.apibiblio.controllers;

import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.DelayLoanException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;
import com.ocdev.biblio.apibiblio.errors.NotEnoughCopiesException;
import com.ocdev.biblio.apibiblio.services.PretService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1")
@Validated
public class PretController
{
	@Autowired PretService pretService;
		
	@ApiOperation(value = "Création d'un prêt", notes = "Création d'un nouveau prêt")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Le prêt est correctement créé"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné et/ou l'ouvrage n'existe pas"),
			@ApiResponse(code = 460, message = "Un prêt en cours existe déjà pour cet ouvrage et cet abonné"),
			@ApiResponse(code = 462, message = "Pas assez d'exemplaires pour le prêt de cet ouvrage")
			})
	@PutMapping(value = "/prets/abonne/{abonneId}/ouvrage/{ouvrageId}", produces = "application/json" )
	public ResponseEntity<Pret> pret(@ApiParam(value = "ID de l'abonné", required = true, example = "1") @PathVariable @Min(1) final Long abonneId, 
			@ApiParam(value = "ID de l'ouvrage", required = true, example = "1") @PathVariable @Min(1) final Long ouvrageId)
					throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException
	{
		Pret result = pretService.creer(abonneId, ouvrageId);
		return new ResponseEntity<Pret>(result, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Retour d'un prêt", notes = "Retour d'un prêt")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Le retour du prêt est enregistré"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "Le prêt n'existe pas"),
			@ApiResponse(code = 469, message = "Seul l'emprunteur ou un employé peuvent retourner un prêt")
			})
	@PutMapping(value ="/prets/retour/{pretId}/utilisateur/{utilisateurId}", produces = "application/json")
	public ResponseEntity<?> retourPret(@ApiParam(value = "ID du prêt", required = true, example = "1")
		@PathVariable @Min(1) final Long pretId, @ApiParam(value = "ID du demandeur", required = true, example = "1")
		@PathVariable @Min(1) final Long utilisateurId) throws EntityNotFoundException, NotAllowedException
	{
		pretService.retournerOuvrage(pretId, utilisateurId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Liste des prêts", notes = "Obtenir la liste des prêts pour un abonné")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste des prêts est retourné dans le corps de la réponse"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné n'existe pas")
			})
	@GetMapping(value = "/prets/{abonneId}", produces = "application/json")
	public ResponseEntity<Page<Pret>> ListeMesPrets(@ApiParam(value = "ID de l'abonné", required = true, example = "1")
			@PathVariable @Min(1) final Long abonneId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "99") int taille) throws EntityNotFoundException
	{
		Pageable paging = PageRequest.of(page,  taille);
		
		Page<Pret> results = pretService.listerSesPrets(abonneId, paging);
		return new ResponseEntity<Page<Pret>>(results, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Prolongation d'un prêt", notes = "Prolongation d'un prêt pour une nouvelle période")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La prolongation du prêt est enregistrée"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "Le prêt n'existe pas"),
			@ApiResponse(code = 461, message = "Le prêt ne peut plus être prolongé"),
			@ApiResponse(code = 469, message = "Seul l'emprunteur ou un employé peuvent prolonger un prêt")
			})
	@PutMapping(value ="/prets/prolonge/{pretId}/utilisateur/{utilisateurId}", produces = "application/json")
	public ResponseEntity<Pret> prolongePret(@ApiParam(value = "ID du prêt", required = true, example = "1")
		@PathVariable @Min(1) final Long pretId, @ApiParam(value = "ID du demandeur", required = true, example = "1")
		@PathVariable @Min(1) final Long utilisateurId)	throws EntityNotFoundException, DelayLoanException, NotAllowedException
	{
		Pret result = pretService.prolonger(pretId, utilisateurId);
		return new ResponseEntity<Pret>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Prêts en retard", notes = "Liste des prêts avec date de fin postérieur à une date")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste est retournée dans le corps de la réponse")
			})
	@GetMapping(value ="/prets/retard", produces = "application/json")
	public ResponseEntity<Collection<Pret>> pretsEnRetard(
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") final Date dateMaxi)
	{
		Collection<Pret> result = pretService.pretsEnRetard(dateMaxi);
		return new ResponseEntity<Collection<Pret>>(result, HttpStatus.OK);
	}
}
