package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.model.Ingrediente;
import com.lucas.sysrestaurant.service.IngredienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService service;

    public IngredienteController(IngredienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ingrediente> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Ingrediente findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ingrediente create(@RequestBody Ingrediente ingrediente) {
        return service.create(ingrediente);
    }

    @PutMapping("/{id}")
    public Ingrediente update(@PathVariable Long id, @RequestBody Ingrediente ingrediente) {
        return service.update(id, ingrediente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
