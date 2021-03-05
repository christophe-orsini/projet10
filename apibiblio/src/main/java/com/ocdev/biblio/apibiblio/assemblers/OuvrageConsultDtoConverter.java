package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dto.OuvrageConsultDto;
import com.ocdev.biblio.apibiblio.entities.Ouvrage;

@Component
public class OuvrageConsultDtoConverter implements IDtoConverter<Ouvrage, OuvrageConsultDto>
{
	@Override
	public Ouvrage convertDtoToEntity(OuvrageConsultDto dto)
	{
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public OuvrageConsultDto convertEntityToDto(Ouvrage entity)
	{
		OuvrageConsultDto ouvrage = new OuvrageConsultDto();
		
		ouvrage.setId(entity.getId());
		ouvrage.setTitre(entity.getTitre());
		ouvrage.setResume(entity.getResume());
		ouvrage.setAuteur(entity.getAuteur());
		ouvrage.setAnneeEdition(entity.getAnneeEdition());
		ouvrage.setNbreExemplaire(entity.getNbreExemplaire());
		ouvrage.setTheme(entity.getTheme().getNom());
		
		return ouvrage;
	}
}
