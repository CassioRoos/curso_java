package com.cassioroos.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cassioroos.cursomc.services.EmailService;
import com.cassioroos.cursomc.services.SMTPEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public EmailService emailService() {
		return new SMTPEmailService();
	}
	
}
