package com.ocdev.biblio.apibiblio.controllers;


import java.security.Principal;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.AvailableException;
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
			@ApiResponse(code = 401, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné et/ou l'ouvrage n'existe pas"),
			@ApiResponse(code = 460, message = "Réservation impossible car une prêt en cours existe déjà pour cet ouvrage et cet abonné"),
			@ApiResponse(code = 462, message = "Pas assez d'exemplaires pour la réservation de cet ouvrage"),
			@ApiResponse(code = 463, message = "Nombre maximum de réservation atteint pour cet ouvrage"),
			@ApiResponse(code = 464, message = "Ouvrage avec exemplaire disponible"),
			@ApiResponse(code = 469, message = "Seul l'abonné ou un employé peuvent reserver un ouvrage")
			})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PutMapping(value = "/reservations/abonne/{abonneId}/ouvrage/{ouvrageId}", produces = "application/json" )
	public ResponseEntity<Pret> reserver(@ApiParam(value = "ID de l'abonné", required = true, example = "1") @PathVariable @Min(1) final Long abonneId, 
			@ApiParam(value = "ID de l'ouvrage", required = true, example = "1") @PathVariable @Min(1) final Long ouvrageId,
			Principal requester)
					throws AlreadyExistsException, EntityNotFoundException, NotEnoughCopiesException, FullWaitingQueueException, NotAllowedException, AvailableException
	{
		String requesterName = requester.getName();
		Pret result = pretService.reserver(abonneId, ouvrageId, requesterName);
		return new ResponseEntity<Pret>(result, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Annulation d'une réservation", notes = "Annulation d'une réservation")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "L'annulation de la réservation est enregistrée"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "La réservation n'existe pas"),
			@ApiResponse(code = 469, message = "Seul la personne qui a réservé ou un employé peuvent annuler une réservation")
			})
	@PutMapping(value ="/reservations/annuler/{reservationId}/utilisateur/{utilisateurId}", produces = "application/json")
	public ResponseEntity<?> annulerReservation(
			@ApiParam(value = "ID de la réservation", required = true, example = "1") @PathVariable @Min(1) final Long reservationId, 
			@ApiParam(value = "ID du demandeur", required = true, example = "1") @PathVariable @Min(1) final Long utilisateurId,
			Principal requester) throws EntityNotFoundException, NotAllowedException
	{
		String requesterName = requester.getName();
		pretService.annulerReservation(reservationId, utilisateurId, requesterName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Liste des réservations", notes = "Obtenir la liste des réservations pour un abonné")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste des réservations est retourné dans le corps de la réponse"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "L'abonné n'existe pas"),
			@ApiResponse(code = 469, message = "Seul l'abonné ou un employé peuvent lister ses réservations")
			})
	@GetMapping(value = "/reservations/{abonneId}", produces = "application/json")
	public ResponseEntity<Page<ReservationDto>> ListerReservations(
			@ApiParam(value = "ID de l'abonné", required = true, example = "1") @PathVariable @Min(1) final Long abonneId,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "99") int taille,
			Principal requester) throws EntityNotFoundException, NotAllowedException
	{
		String requesterName = requester.getName();
		List<ReservationDto> list = (List<ReservationDto>) pretService.listerReservationsAbonne(abonneId, requesterName);
        
		return new ResponseEntity<Page<ReservationDto>>(new PageImpl<ReservationDto>(list), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retirer une réservation devenue disponible", notes = "Permet de retirer une réservation devenue disponible")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Le prêt est enregistré"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			@ApiResponse(code = 404, message = "La réservation n'existe pas"),
			@ApiResponse(code = 469, message = "Seul la personne qui a réservé ou un employé peuvent retirer une réservation")
			})
	@PutMapping(value ="/reservations/retirer/{reservationId}/utilisateur/{utilisateurId}", produces = "application/json")
	public ResponseEntity<?> retirerReservation(
			@ApiParam(value = "ID de la réservation", required = true, example = "1") @PathVariable @Min(1) final Long reservationId,
			@ApiParam(value = "ID du demandeur", required = true, example = "1") @PathVariable @Min(1) final Long utilisateurId,
			Principal requester) throws EntityNotFoundException, NotAllowedException
	{
		String requesterName = requester.getName();
		pretService.retirerReservation(reservationId, utilisateurId, requesterName);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Réservations disponibles", notes = "Liste des réservations disponibles pour envoi d'email")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste est retournée dans le corps de la réponse"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			})
	@GetMapping(value ="/reservations/disponible", produces = "application/json")
	public ResponseEntity<Collection<Pret>> reservationsDisponibles()
	{
		Collection<Pret> result = pretService.reservationsDisponibles();
		return new ResponseEntity<Collection<Pret>>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Enregistrer l'envoi d'un email pour une liste de réservations", notes = "Enregistre l'envoi d'un email par le traitement batch lorsque une réservation est devenue disponible")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "L'envoi d'email est enregistré pour la liste"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			})
	@PostMapping(value ="/reservations/enregistrer/emails/envoyes", produces = "application/json")
	public ResponseEntity<?> enregistrerEmailEnvoye(@Valid @RequestBody final Collection<Long> reservationsIDs)
	{
		pretService.setEmailsEnvoyes(reservationsIDs);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation(value = "Annuler une liste de réservations échues", notes = "Annule une liste de réservations échues")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "La liste des réservations est annulée"),
			@ApiResponse(code = 401, message = "Authentification requise"),
			})
	@PostMapping(value ="/reservations/annuler", produces = "application/json")
	public ResponseEntity<?> annulerReservations(@Valid @RequestBody final Collection<Long> reservationsIDs)
	{
		pretService.annulerReservations(reservationsIDs);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}