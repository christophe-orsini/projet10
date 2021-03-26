package com.ocdev.biblio.apibiblio.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ocdev.biblio.apibiblio.assemblers.IDtoConverter;
import com.ocdev.biblio.apibiblio.dao.UtilisateurRepository;
import com.ocdev.biblio.apibiblio.dto.UtilisateurCreateDto;
import com.ocdev.biblio.apibiblio.entities.Role;
import com.ocdev.biblio.apibiblio.entities.Utilisateur;
import com.ocdev.biblio.apibiblio.errors.AlreadyExistsException;
import com.ocdev.biblio.apibiblio.errors.EntityNotFoundException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService
{
	private UtilisateurRepository utilisateurRepository;
	private IDtoConverter<Utilisateur, UtilisateurCreateDto> utilisateurConverter;
	private BCryptPasswordEncoder passwordencoder;
	
	public UtilisateurServiceImpl(
			@Autowired UtilisateurRepository utilisateurRepository,
			@Autowired IDtoConverter<Utilisateur, UtilisateurCreateDto> utilisateurConverter,
			@Autowired BCryptPasswordEncoder passwordencoder)
	{
		this.utilisateurRepository = utilisateurRepository;
		this.utilisateurConverter = utilisateurConverter;
		this.passwordencoder = passwordencoder;
	}
	
	@Override
	public Utilisateur creer(UtilisateurCreateDto utilisateurDto) throws AlreadyExistsException
	{
		Optional<Utilisateur> utilisateurExists = utilisateurRepository.findByEmailIgnoreCase(utilisateurDto.getEmail());
		if (utilisateurExists.isPresent()) throw new AlreadyExistsException("Un utilisateur avec cet email existe déjà");
		
		String passwordEncoded = passwordencoder.encode(utilisateurDto.getPassword());
		
		Utilisateur utilisateur = utilisateurConverter.convertDtoToEntity(utilisateurDto);
		utilisateur.setRole(Role.ROLE_ABONNE);
		utilisateur.setPassword(passwordEncoded);
		
		// log
		return utilisateurRepository.save(utilisateur);
	}

	@Override
	public Utilisateur obtenir(String email) throws EntityNotFoundException
	{
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmailIgnoreCase(email);
		if (!utilisateur.isPresent()) throw new EntityNotFoundException("L'utilisateur n'existe pas");
		
		return utilisateur.get();
	}
}
