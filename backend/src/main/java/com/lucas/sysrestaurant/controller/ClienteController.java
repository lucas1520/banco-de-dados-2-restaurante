package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.ContaClienteResponse;
import com.lucas.sysrestaurant.dto.PagarClienteResponse;
import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente cliente) {
        return service.create(cliente);
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return service.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/conta")
    public ContaClienteResponse conta(@PathVariable Long id) {
        return service.conta(id);
    }

    @PostMapping("/{id}/pagar")
    public PagarClienteResponse pagar(@PathVariable Long id) {
        return service.pagar(id);
    }
}
