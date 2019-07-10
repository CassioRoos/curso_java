package com.cassioroos.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassioroos.cursomc.domain.ItemPedido;
import com.cassioroos.cursomc.domain.PagamentoComBoleto;
import com.cassioroos.cursomc.domain.Pedido;
import com.cassioroos.cursomc.domain.enums.EstadoPagamento;
import com.cassioroos.cursomc.repositories.ItemPedidoRepository;
import com.cassioroos.cursomc.repositories.PagamentoRepository;
import com.cassioroos.cursomc.repositories.PedidoRepository;
import com.cassioroos.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFountException(
				"Objeto não encontrado! id : " + id + " tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto ) {
			PagamentoComBoleto boleto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(boleto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
	}
}
