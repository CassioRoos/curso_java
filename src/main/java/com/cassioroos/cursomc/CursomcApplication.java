package com.cassioroos.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cassioroos.cursomc.domain.Categoria;
import com.cassioroos.cursomc.domain.Cidade;
import com.cassioroos.cursomc.domain.Cliente;
import com.cassioroos.cursomc.domain.Endereco;
import com.cassioroos.cursomc.domain.Estado;
import com.cassioroos.cursomc.domain.ItemPedido;
import com.cassioroos.cursomc.domain.Pagamento;
import com.cassioroos.cursomc.domain.PagamentoComBoleto;
import com.cassioroos.cursomc.domain.PagamentoComCartao;
import com.cassioroos.cursomc.domain.Pedido;
import com.cassioroos.cursomc.domain.Produto;
import com.cassioroos.cursomc.domain.enums.EstadoPagamento;
import com.cassioroos.cursomc.domain.enums.TipoCliente;
import com.cassioroos.cursomc.repositories.CategoriaRepository;
import com.cassioroos.cursomc.repositories.CidadeRepository;
import com.cassioroos.cursomc.repositories.ClienteRepository;
import com.cassioroos.cursomc.repositories.EnderecoRepository;
import com.cassioroos.cursomc.repositories.EstadoRepository;
import com.cassioroos.cursomc.repositories.ItemPedidoRepository;
import com.cassioroos.cursomc.repositories.PagamentoRepository;
import com.cassioroos.cursomc.repositories.PedidoRepository;
import com.cassioroos.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Jardinagem");
		Categoria cat4 = new Categoria(null, "Decoração");
		Categoria cat5 = new Categoria(null, "Perfumaria");
		Categoria cat6 = new Categoria(null, "Celulares");
		Categoria cat7 = new Categoria(null, "Inside");
		Categoria cat8 = new Categoria(null, "Patio furniture");
		Categoria cat9 = new Categoria(null, "Pets");
		Categoria cat10 = new Categoria(null, "Laticinios");
		Categoria cat11 = new Categoria(null, "Cama, Mesa e Banho");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		Estado est1 = new Estado(null, "Minas gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		Cliente cli1 = new Cliente(null, "Maria", "maria@gmail.com", "99988866655", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("9988774433", "8877221133"));

		Endereco e1 = new Endereco(null, "Rua flores", "300", "AP 401", "Centro", "99060666", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua matos", "500", "AP 211", "Uberaba", "33060555", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);

		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);

		ped1.setPagamento(pgto1);

		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"),
				null);
		ped2.setPagamento(pgto2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 2000.00);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, cat9, cat10, cat11));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}

}
