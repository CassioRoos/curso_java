package com.cassioroos.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cassioroos.cursomc.domain.Pedido;
import com.cassioroos.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id){
		
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);
		/*Pedido cat1 = new Pedido(1, "Info");
		Pedido cat2 = new Pedido(2, "Cel");
		
		List<Pedido> lista = new  ArrayList<Pedido>(); 
		
		lista.add(cat1);
		lista.add(cat2);
		return lista;*/
	}
	

}
