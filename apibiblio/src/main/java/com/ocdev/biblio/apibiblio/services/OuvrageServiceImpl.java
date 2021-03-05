package com.ocdev.biblio.apibiblio.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.criterias.OuvrageCriteria;
import com.ocdev.biblio.apibiblio.criterias.OuvrageSpecification;
import com.ocdev.biblio.apibiblio.dao.OuvrageRepository;
import com.ocdev.biblio.apibiblio.dao.PretRepository;
import com.ocdev.biblio.apibiblio.dao.ThemeRepository;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Role;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.errors.NotAllowedException;
import com.ocdev.biblio.apibiblio.utils.AppSettings;

@Service
public class OuvrageServiceImpl implements OuvrageService
{
	@Autowired private OuvrageRepository ouvrageRepository;
	@Autowired private ThemeRepository themeRepository;
	@Autowired private UtilisateurRepository utilisateurRepository;
	@Autowired private PretRepository pretRepository;
	@Autowired private IDtoConverter<Ouvrage, OuvrageCreateDto> ouvrageConverter;
	@Autowired private IDtoConverter<Ouvrage, OuvrageConsultDto> ouvrageConsultConverter;
	
	@Override
	public Ouvrage creer(OuvrageCreateDto ouvrageCreateDto) throws AlreadyExistsException, EntityNotFoundException
	{
		Optional<Ouvrage> ouvrageExists = ouvrageRepository.findByTitreIgnoreCase(ouvrageCreateDto.getTitre());
		if (ouvrageExists.isPresent())
		{
			// un ouvrage avec ce titre existe déjà
			// log
			throw new AlreadyExistsException("Un ouvrage avec le même titre existe déjà");
		}
		
		// Controle du theme
		Optional<Theme> theme = themeRepository.findById(ouvrageCreateDto.getTheme());
		if (!theme.isPresent())
		{
			// le theme n'existe pas
			// log
			throw new EntityNotFoundException("Ce thème n'existe pas");	
		}
		
		Ouvrage ouvrage = ouvrageConverter.convertDtoToEntity(ouvrageCreateDto);
				
		// log
		return ouvrageRepository.save(ouvrage);
	}

	@Override
	public Page<Ouvrage> rechercherOuvrages(OuvrageCriteria critere, Pageable paging)
	{
		OuvrageSpecification spec = new OuvrageSpecification(critere);
		return ouvrageRepository.findAll(spec, paging);
	}

	@Override
	public OuvrageConsultDto consulterOuvrage(long ouvrageId, long utilisateurId, String requesterName) throws EntityNotFoundException, NotAllowedException
	{
		Optional<Ouvrage> ouvrage = ouvrageRepository.findById(ouvrageId);
		if (!ouvrage.isPresent()) throw new EntityNotFoundException("L'ouvrage n'existe pas");
		
		Optional<Utilisateur> utilisateur = utilisateurRepository.findById(utilisateurId);
		if (!utilisateur.isPresent()) throw new EntityNotFoundException("L'utilisateur n'existe pas");
		
		// verifier si le demandeur est l'emprunteur ou un employé
		Utilisateur requester = utilisateurRepository.findByEmailIgnoreCase(requesterName).
				orElseThrow(() -> new NotAllowedException("Vous n'etes pas correctement authentifié"));
		if (requester.getRole() == Role.ROLE_ABONNE && !utilisateur.get().getEmail().equals(requesterName))
			throw new NotAllowedException("Vous ne pouvez pas consulter cet ouvrage. Vous n'etes pas l'abonné");
				
		OuvrageConsultDto result = ouvrageConsultConverter.convertEntityToDto(ouvrage.get());
		result.setReservable(false);
		
		// Date prochain retour
		Optional<Pret> pret = pretRepository.findFirstPretByOuvrageId(ouvrageId);
		if (pret.isPresent())
		{
			result.setProchainRetour(pret.get().getDateFinPrevu());
		}
		else
		{
			result.setProchainRetour(new Date());	
		}
		
		// nbre reservation
		int nbreReservations = pretRepository.findAllReservationsByOuvrageId(ouvrageId).size();
		result.setNbreReservations(nbreReservations);
		
		// nbre max de reservations atteint
		int nbreMaxiReservation = ouvrage.get().getNbreExemplaireTotal() * AppSettings.getIntSetting("reservation.multiple");
		if (nbreReservations >= nbreMaxiReservation) return result;
		
		// exemplaire disponible
		if (ouvrage.get().getNbreExemplaire() > 0) return result;
		
		// reservable
		pret = pretRepository.findByAbonneIdAndOuvrageIdAndEnPretOrReserve(utilisateurId, ouvrageId);
		if (pret.isPresent()) return result;
		
		result.setReservable(true);
		return result;
	}
}
