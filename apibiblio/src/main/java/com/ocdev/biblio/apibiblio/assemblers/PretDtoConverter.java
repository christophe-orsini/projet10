package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dao.OuvrageRepository;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.PretDto;
import com.ocdev.biblio.apibiblio.entities.Pret;
import com.ocdev.biblio.apibiblio.entities.Statut;

@Component
public class PretDtoConverter implements IDtoConverter<Pret, PretDto>
{
	@Autowired UtilisateurRepository utilisateurRepository;
	@Autowired OuvrageRepository ouvrageRepository;

	@Override
	public Pret convertDtoToEntity(PretDto pretDto)
	{
		Pret pret = new Pret();
		
		pret.setId(pretDto.getId());
		pret.setDateDebut(pretDto.getDateDebut());
		pret.setDateFinPrevu(pretDto.getDateFinPrevu());
		pret.setDateRetour(pretDto.getDateRetour());
		pret.setStatut(Statut.convert(pretDto.getStatut()));
		pret.setAbonne(utilisateurRepository.getOne(pretDto.getAbonneId()));
		pret.setOuvrage(ouvrageRepository.getOne(pretDto.getOuvrageId()));
		
		return pret;
	}

	@Override
	public PretDto convertEntityToDto(Pret entity)
	{
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}
}
