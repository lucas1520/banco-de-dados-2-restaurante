package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.model.Ingrediente;
import com.lucas.sysrestaurant.repository.IngredienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IngredienteService {

    private final IngredienteRepository repository;

    public IngredienteService(IngredienteRepository repository) {
        this.repository = repository;
    }

    public List<Ingrediente> findAll() {
        return repository.findAll();
    }

    public Ingrediente findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingrediente não encontrado: " + id));
    }

    public Ingrediente create(Ingrediente ingrediente) {
        ingrediente.setIdIngrediente(null);
        return repository.save(ingrediente);
    }

    public Ingrediente update(Long id, Ingrediente ingrediente) {
        findById(id);
        ingrediente.setIdIngrediente(id);
        return repository.save(ingrediente);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
