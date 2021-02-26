package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dto.OuvrageCreateDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;
import com.ocdev.biblio.apibiblio.entities.Theme;

@Component
public class OuvrageCreateDtoConverter implements IDtoConverter<Ouvrage, OuvrageCreateDto>
{
	@Override
	public Ouvrage convertDtoToEntity(OuvrageCreateDto ouvrageCreateDto)
	{
		Ouvrage ouvrage = new Ouvrage();
		
		ouvrage.setTitre(ouvrageCreateDto.getTitre());
		ouvrage.setAuteur(ouvrageCreateDto.getAuteur());
		ouvrage.setResume(ouvrageCreateDto.getResume());
		ouvrage.setAnneeEdition(ouvrageCreateDto.getAnneeEdition());
		
		Theme theme = new Theme();
		theme.setId(ouvrageCreateDto.getTheme());
	
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