package com.ocdev.biblio.batch.services;

import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService
{
	@Autowired PropertiesConfigurationService properties;
	
	@Override
    public void envoiEmailHtml(String to, String content, String subject, String from) throws MessagingException
	{
		JavaMailSender emailSender = getJavaMailSender();
				
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);
        helper.setFrom(from);
        emailSender.send(message);
    }
	
	private JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(properties.host());
		mailSenderImpl.setPort(properties.port());
		mailSenderImpl.setUsername(properties.username());
		mailSenderImpl.setPassword(properties.emailPassword());
		return mailSenderImpl;
	}
}
