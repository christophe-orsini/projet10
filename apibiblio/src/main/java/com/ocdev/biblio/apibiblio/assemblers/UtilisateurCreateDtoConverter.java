package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dto.UtilisateurCreateDto;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;

@Component
public class UtilisateurCreateDtoConverter implements IDtoConverter<Utilisateur, UtilisateurCreateDto>
{
	@Override
	public Utilisateur convertDtoToEntity(UtilisateurCreateDto utilisateurDto)
	{
		Utilisateur utilisateur = new Utilisateur();
		
		utilisateur.setEmail(utilisateurDto.getEmail());
		utilisateur.setPassword(utilisateurDto.getPassword());
		utilisateur.setNom(utilisateurDto.getNom());
		utilisateur.setPrenom(utilisateurDto.getPrenom());
		
		return utilisateur;
	}

	@Override
	public UtilisateurCreateDto convertEntityToDto(Utilisateur entity)
	{
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}
}