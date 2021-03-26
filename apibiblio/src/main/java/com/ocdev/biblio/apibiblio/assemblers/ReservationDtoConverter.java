package com.ocdev.biblio.apibiblio.assemblers;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Component;
import com.ocdev.biblio.apibiblio.dto.ReservationDto;
import com.ocdev.biblio.apibiblio.entities.Pret;

@Component
public class ReservationDtoConverter implements IDtoConverter<Pret, ReservationDto>
{
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
		reservation.setDateHeureExpiration(entity.getDateHeureExpiration());
		reservation.setEmailEnvoye(entity.isEmailEnvoye());
		reservation.setStatut(entity.getStatut());
		reservation.setAbonneId(entity.getAbonne().getId());
		reservation.setOuvrage(entity.getOuvrage());
		
		return reservation;
	}
}
