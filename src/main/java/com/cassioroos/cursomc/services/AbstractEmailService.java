package com.cassioroos.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.cassioroos.cursomc.domain.Cliente;
import com.cassioroos.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleEmailMessageFromPedido(obj);
		SendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleEmailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setSubject("Pedido confirmado! Número: " + obj.getId());
		sm.setFrom(sender);
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass);
		SendEmail(sm);
	}

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(cliente.getEmail());
		sm.setSubject("Solictação nova senha!");
		sm.setFrom(sender);
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText("Nova senha : " + newPass);
		return sm;
	}

}
