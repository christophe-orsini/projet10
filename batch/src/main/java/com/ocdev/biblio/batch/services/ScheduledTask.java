package com.ocdev.biblio.batch.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ocdev.biblio.batch.model.Pret;
import com.ocdev.biblio.batch.model.Utilisateur;

@Component
public class ScheduledTask
{
	@Autowired PretService pretService;
	@Autowired EmailService emailService;
	@Autowired EmailContentBuilder contentBuilder;
	@Autowired PropertiesConfigurationService properties;
	
	public ScheduledTask()
	{
		super();
	}
	public ScheduledTask(PretService pretService, EmailService emailService)
	{
		super();
		this.pretService = pretService;
		this.emailService = emailService;
	}
	
	@Scheduled(cron = "${app.cron.expression}")
	public void cronTask() throws AddressException, MessagingException, IOException
	{
		Collection<Pret> pretsEnRetard = pretService.listePretsEnCoursADate(new Date());
		Collection<Utilisateur> abonnes = pretService.pretsParAbonne(pretsEnRetard);
		for (Utilisateur abonne : abonnes)
		{
			String emailContent = contentBuilder.buildDelayEmail(abonne);
			emailService.envoiEmailHtml(
					abonne.getEmail(),
					emailContent,
					properties.emailSubject(),
					properties.emailContact());
		}
	}
	
	@Scheduled(cron = "${app.cron.reservation.expression}")
	public void cronReservationTask() throws AddressException, MessagingException, IOException
	{
		Collection<Pret> reservationsDisponibles = pretService.listeReservationsDisponibles();
		
		Collection<Pret> emailsAEnvoyer = pretService.listeEmailsAEnvoyer(reservationsDisponibles);
		Collection<Utilisateur> abonnes = pretService.pretsParAbonne(emailsAEnvoyer);
		for (Utilisateur abonne : abonnes)
		{
			String emailContent = contentBuilder.buildReservationEmail(abonne);
			emailService.envoiEmailHtml(
					abonne.getEmail(),
					emailContent,
					properties.emailReservationSubject(),
					properties.emailContact());
		}
		
		pretService.SetEmailEnvoyé(emailsAEnvoyer);
		
		Collection<Pret> reservationsEchues = pretService.listeReservationsEchues(reservationsDisponibles);
		pretService.annulerReservations(reservationsEchues);
	}
}
