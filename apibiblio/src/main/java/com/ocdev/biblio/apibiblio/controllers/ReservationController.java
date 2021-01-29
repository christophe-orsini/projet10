package com.ocdev.biblio.apibiblio.controllers;


import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.FullWaitingQueueException;
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
public class ReservationController
{
	@Autowired PretService pretService;
		
	@ApiOperation(value = "Réservation d'un ouvrage", notes = "Réservation d'un ouvrage")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "La réservation est enregistrée"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné et/ou l'ouvrage n'existe pas"),
			@ApiResponse(code = 460, message = "Réservation impossible car une prêt en cours existe déjà pour cet ouvrage et cet abonné"),
			@ApiResponse(code = 462, message = "Pas assez d'exemplaires pour la réservation de cet ouvrage"),
			@ApiResponse(code = 463, message = "Nombre maximum de réservation atteint pour cet ouvrage")
			})
	@PutMapping(value = "/reservations/abonne/{abonneId}/ouvrage/{ouvrageId}", produces = "application/json" )
	public ResponseEntity<Pret> reservation(@ApiParam(value = "ID de l'abonné", required = true, example = "1") @PathVariable @Min(1) final Long abonneId, 
			@ApiParam(value = "ID de l'ouvrage", required = true, example = "1") @PathVariable @Min(1) final Long ouvrageId)
					throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, FullWaitingQueueException
	{
		Pret result = pretService.reserver(abonneId, ouvrageId);
		return new ResponseEntity<Pret>(result, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Annulation d'une réservation", notes = "Annulation d'une réservation")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "L'annulationde la réservation est enregistrée"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "La réservation n'existe pas"),
			@ApiResponse(code = 469, message = "Seul la personne qui a réservé ou un employé peuvent annuler une réservation")
			})
	@PutMapping(value ="/reservations/annuler/{reservationId}/utilisateur/{utilisateurId}", produces = "application/json")
	public ResponseEntity<?> annulationReservation(@ApiParam(value = "ID de la réservation", required = true, example = "1")
		@PathVariable @Min(1) final Long reservationId, @ApiParam(value = "ID du demandeur", required = true, example = "1")
		@PathVariable @Min(1) final Long utilisateurId) throws EntityNotFoundException, NotAllowedException
	{
		pretService.annulerReservation(reservationId, utilisateurId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Liste des réservations", notes = "Obtenir la liste des réservations pour un abonné")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste des réservations est retourné dans le corps de la réponse"),
			@ApiResponse(code = 403, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné n'existe pas")
			})
	@GetMapping(value = "/reservations/{abonneId}", produces = "application/json")
	public ResponseEntity<Page<Pret>> ListeMesPrets(@ApiParam(value = "ID de l'abonné", required = true, example = "1")
			@PathVariable @Min(1) final Long abonneId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "99") int taille) throws EntityNotFoundException
	{
		Pageable paging = PageRequest.of(page,  taille);
		
		Page<Pret> results = pretService.listerSesReservations(abonneId, paging);
		return new ResponseEntity<Page<Pret>>(results, HttpStatus.OK);
	}
}
