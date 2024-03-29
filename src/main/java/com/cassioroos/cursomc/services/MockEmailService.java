package com.cassioroos.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

	private static final Logger log = LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void SendEmail(SimpleMailMessage msg) {
		log.info("Enviando email");
		log.info(msg.toString());
		log.info("------------Email enviado------------");
		
	}

}
