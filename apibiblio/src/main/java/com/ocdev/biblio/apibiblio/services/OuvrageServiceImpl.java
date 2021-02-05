package com.ocdev.biblio.apibiblio.services;

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
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

@Service
public class OuvrageServiceImpl implements OuvrageService
{
	@Autowired private OuvrageRepository ouvrageRepository;
	@Autowired private UtilisateurRepository utilisateurRepository;
	@Autowired private PretRepository pretRepository;
	@Autowired private IDtoConverter<Ouvrage, OuvrageCreateDto> ouvrageConverter;
	@Autowired private IDtoConverter<Ouvrage, OuvrageConsultDto> ouvrageConsultConverter;
	
	@Override
	public Ouvrage creer(OuvrageCreateDto ouvrageCreateDto) throws AlreadyExistsException
	{
		Optional<Ouvrage> ouvrageExists = ouvrageRepository.findByTitreIgnoreCase(ouvrageCreateDto.getTitre());
		if (ouvrageExists.isPresent())
		{
			// un ouvrage avec ce titre existe déjà
			// log
			throw new AlreadyExistsException("Un ouvrage avec le même titre existe déjà");
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
	public OuvrageConsultDto consulterOuvrage(long ouvrageId, long utilisateurId) throws EntityNotFoundException
	{
		Optional<Ouvrage> ouvrage = ouvrageRepository.findById(ouvrageId);
		if (!ouvrage.isPresent()) throw new EntityNotFoundException("L'ouvrage n'existe pas");
		
		Optional<Utilisateur> utilisateur = utilisateurRepository.findById(utilisateurId);
		if (!utilisateur.isPresent()) throw new EntityNotFoundException("L'utilisateur n'existe pas");
		
		OuvrageConsultDto result = ouvrageConsultConverter.convertEntityToDto(ouvrage.get());
		result.setReservable(false);
		
		if (ouvrage.get().getNbreExemplaire() > 0) return result;
		
		Optional<Pret> pret = pretRepository.findByAbonneIdAndOuvrageIdAndEnPret(utilisateurId, ouvrageId);
		if (pret.isPresent()) return result;
		
		pret = pretRepository.findByAbonneIdAndOuvrageIdAndReserve(utilisateurId, ouvrageId);
		if (pret.isPresent()) return result;
		
		result.setReservable(true);
		return result;
	}
}
