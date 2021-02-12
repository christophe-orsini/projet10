package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dao.OuvrageRepository;
import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Pret;

@Component
public class ReservationDtoConverter implements IDtoConverter<Pret, ReservationDto>
{
	@Autowired OuvrageRepository ouvrageRepository;

	@Override
	public Pret convertDtoToEntity(ReservationDto reservationDto)
	{
		// TODO Auto-generated method stub
			throw new NotYetImplementedException();
		
	}

	@Override
	public ReservationDto convertEntityToDto(Pret entity)
	{
		ReservationDto reservation = new ReservationDto();
		
		reservation.setId(entity.getId());
		reservation.setDateHeureReservation(entity.getDateHeureReservation());
		reservation.setAbonneId(entity.getAbonne().getId());
		reservation.setOuvrage(ouvrageRepository.getOne(entity.getOuvrage().getId()));
		
		return reservation;
	}
}