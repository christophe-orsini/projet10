package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.dto.ThemeCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Theme;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;
import com.ocdev.biblio.apibiblio.services.ThemeService;

@Component
public class OuvrageCreateDtoConverter implements IDtoConverter<Ouvrage, OuvrageCreateDto>
{
	@Autowired ThemeService themeService;
	
	@Override
	public Ouvrage convertDtoToEntity(OuvrageCreateDto ouvrageCreateDto)
	{
		Ouvrage ouvrage = new Ouvrage();
		
		ouvrage.setTitre(ouvrageCreateDto.getTitre());
		ouvrage.setAuteur(ouvrageCreateDto.getAuteur());
		ouvrage.setResume(ouvrageCreateDto.getResume());
		ouvrage.setAnneeEdition(ouvrageCreateDto.getAnneeEdition());
		Theme theme = null;
		try
		{
			theme = themeService.obtenir(ouvrageCreateDto.getTheme());
		}
		catch (EntityNotFoundException e)
		{
			try
			{
				theme = themeService.obtenir("Inconnu");
			}
			catch (EntityNotFoundException e2)
			{
				try
				{
					ThemeCreateDto themeCreateDto = new ThemeCreateDto();
					themeCreateDto.setNom("Inconnu");
					theme = themeService.creer(themeCreateDto);
				}
				catch (AlreadyExistsException e3)
				{
					// Impossible
					// TODO: handle exception
					// log
				}
			}
		}
		ouvrage.setTheme(theme);
		ouvrage.setNbreExemplaireTotal(ouvrageCreateDto.getNbreExemplaireTotal());
		
		return ouvrage;
	}

		@Override
	public OuvrageCreateDto convertEntityToDto(Ouvrage entity)
	{
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}
}