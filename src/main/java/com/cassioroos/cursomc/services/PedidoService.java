package com.cassioroos.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cassioroos.cursomc.domain.Pedido;
import com.cassioroos.cursomc.repositories.PedidoRepository;
import com.cassioroos.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFountException(
				"Objeto não encontrado! id : " + id + " tipo: " + Pedido.class.getName()));
	}
}
