package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.AbrirMesaRequest;
import com.lucas.sysrestaurant.dto.PedidoContaResponse;
import com.lucas.sysrestaurant.dto.PratoQuantidadeResponse;
import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.Mesa;
import com.lucas.sysrestaurant.service.MesaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService service;

    public MesaController(MesaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mesa> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mesa findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mesa create(@RequestBody Mesa mesa) {
        return service.create(mesa);
    }

    @PutMapping("/{id}")
    public Mesa update(@PathVariable Long id, @RequestBody Mesa mesa) {
        return service.update(id, mesa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/pratos")
    public List<PratoQuantidadeResponse> pratos(@PathVariable Long id) {
        return service.pratosDaMesa(id);
    }

    @GetMapping("/{id}/pedidos")
    public List<PedidoContaResponse> pedidos(@PathVariable Long id) {
        return service.pedidosDaMesa(id);
    }

    @PostMapping("/{id}/abrir")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente abrir(@PathVariable Long id, @RequestBody AbrirMesaRequest request) {
        return service.abrirMesa(id, request);
    }
}
