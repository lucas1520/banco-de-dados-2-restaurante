package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Funcionario> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Funcionario findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario create(@RequestBody Funcionario funcionario) {
        return service.create(funcionario);
    }

    @PutMapping("/{id}")
    public Funcionario update(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        return service.update(id, funcionario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
