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
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

@Service
public class OuvrageServiceImpl implements OuvrageService
{
	@Autowired private OuvrageRepository ouvrageRepository;
	@Autowired private IDtoConverter<Ouvrage, OuvrageCreateDto> ouvrageConverter;
	
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
	public Ouvrage consulterOuvrage(Long id) throws EntityNotFoundException
	{
		Optional<Ouvrage> ouvrage = ouvrageRepository.findById(id);
		if (!ouvrage.isPresent()) throw new EntityNotFoundException("L'ouvrage n'existe pas");
		
		return ouvrage.get();
	}
}
