package com.cassioroos.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cassioroos.cursomc.domain.Categoria;
import com.cassioroos.cursomc.domain.Produto;
import com.cassioroos.cursomc.repositories.CategoriaRepository;
import com.cassioroos.cursomc.repositories.ProdutoRepository;
import com.cassioroos.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository repoCategoria;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFountException(
				"Objeto não encontrado! id : " + id + " tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> findPage(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = repoCategoria.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
