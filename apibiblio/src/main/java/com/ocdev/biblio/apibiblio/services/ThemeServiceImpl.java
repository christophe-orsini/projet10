package com.ocdev.biblio.apibiblio.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ocdev.biblio.apibiblio.assemblers.ThemeCreateDtoConverter;
import com.ocdev.biblio.apibiblio.dao.ThemeRepository;
import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

@Service
public class ThemeServiceImpl implements ThemeService
{
	@Autowired ThemeRepository themeRepository;
	@Autowired ThemeCreateDtoConverter themeConverter;
	
	@Override
	public Theme creer(ThemeCreateDto themeCreateDto) throws AlreadyExistsException
	{
		Optional<Theme> themeExists = themeRepository.findByNomIgnoreCase(themeCreateDto.getNom());
		if (themeExists.isPresent())
		{
			// un theme avec ce nom existe déjà
			// log
			throw new AlreadyExistsException("Un thème avec le même nom existe déjà");
		}
		
		Theme theme = themeConverter.convertDtoToEntity(themeCreateDto);
		
		// log
		return themeRepository.save(theme);
	}

	@Override
	public Theme obtenir(Long id) throws EntityNotFoundException
	{
		Optional<Theme> theme = themeRepository.findById(id);
		if (!theme.isPresent()) throw new EntityNotFoundException("Le thème n'existe pas");
		
		return theme.get();
	}

	@Override
	public Theme obtenir(String nom) throws EntityNotFoundException
	{
		Optional<Theme> theme = themeRepository.findByNomIgnoreCase(nom);
		if (!theme.isPresent()) throw new EntityNotFoundException("Le thème n'existe pas");
		
		return theme.get();
	}

	@Override
	public Page<Theme> listeThemes(Pageable paging)
	{
		return themeRepository.findAll(paging);
	}

}
