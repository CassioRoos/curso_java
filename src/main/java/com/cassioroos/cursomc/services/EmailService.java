package com.cassioroos.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.cassioroos.cursomc.domain.Cliente;
import com.cassioroos.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void SendEmail(SimpleMailMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
