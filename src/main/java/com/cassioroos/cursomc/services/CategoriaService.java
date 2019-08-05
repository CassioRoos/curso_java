package com.cassioroos.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cassioroos.cursomc.DTO.CategoriaDTO;
import com.cassioroos.cursomc.domain.Categoria;
import com.cassioroos.cursomc.repositories.CategoriaRepository;
import com.cassioroos.cursomc.services.exceptions.DataIntegrityException;
import com.cassioroos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {
        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! id : " + id + " tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return repo.save(obj);
    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Categoria update(Categoria obj) {
        Categoria newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }


    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public void delete(Integer cod) {
        Categoria obj = find(cod);
        try {
            repo.delete(obj);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível deletar uma categoria que tenha produtos associados.");
        }
    }

    public Categoria fromDTO(CategoriaDTO obj) {
        return new Categoria(obj.getId(), obj.getNome());
    }

    private void updateData(Categoria newObj, Categoria obj) {
        newObj.setNome(obj.getNome());
    }
}
