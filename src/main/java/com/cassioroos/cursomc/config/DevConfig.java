package com.cassioroos.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cassioroos.cursomc.services.DBService;
import com.cassioroos.cursomc.services.EmailService;
import com.cassioroos.cursomc.services.SMTPEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public Boolean instatiateDataBase() throws ParseException {
		if (!strategy.equals("create")) {
			return false;
		}
		
		dbService.instatiateTestDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SMTPEmailService();
	}
	
}
