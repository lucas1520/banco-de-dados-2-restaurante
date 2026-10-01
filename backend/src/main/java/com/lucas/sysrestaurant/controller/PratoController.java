package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.PratoPossivelResponse;
import com.lucas.sysrestaurant.model.Prato;
import com.lucas.sysrestaurant.service.PratoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pratos")
public class PratoController {

    private final PratoService service;

    public PratoController(PratoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prato> findAll() {
        return service.findAll();
    }

    @GetMapping("/possiveis")
    public List<PratoPossivelResponse> possiveis() {
        return service.possiveis();
    }

    @GetMapping("/{id}")
    public Prato findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prato create(@RequestBody Prato prato) {
        return service.create(prato);
    }

    @PutMapping("/{id}")
    public Prato update(@PathVariable Long id, @RequestBody Prato prato) {
        return service.update(id, prato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
