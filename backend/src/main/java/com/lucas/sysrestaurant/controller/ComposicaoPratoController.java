package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.service.ComposicaoPratoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/composicoes")
public class ComposicaoPratoController {

    private final ComposicaoPratoService service;

    public ComposicaoPratoController(ComposicaoPratoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComposicaoPrato> findAll() {
        return service.findAll();
    }

    @GetMapping("/{idPrato}/{idIngrediente}")
    public ComposicaoPrato findById(@PathVariable Long idPrato, @PathVariable Long idIngrediente) {
        return service.findById(idPrato, idIngrediente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComposicaoPrato create(@RequestBody ComposicaoPrato composicaoPrato) {
        return service.create(composicaoPrato);
    }

    @PutMapping("/{idPrato}/{idIngrediente}")
    public ComposicaoPrato update(@PathVariable Long idPrato, @PathVariable Long idIngrediente,
                                  @RequestBody ComposicaoPrato composicaoPrato) {
        return service.update(idPrato, idIngrediente, composicaoPrato);
    }

    @DeleteMapping("/{idPrato}/{idIngrediente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idPrato, @PathVariable Long idIngrediente) {
        service.delete(idPrato, idIngrediente);
    }
}
