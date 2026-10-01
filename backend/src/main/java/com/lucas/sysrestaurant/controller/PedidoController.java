package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.AdicionarItemRequest;
import com.lucas.sysrestaurant.dto.AlterarItemRequest;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.service.ItemPedidoService;
import com.lucas.sysrestaurant.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;
    private final ItemPedidoService itemPedidoService;

    public PedidoController(PedidoService service, ItemPedidoService itemPedidoService) {
        this.service = service;
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping
    public List<Pedido> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido create(@RequestBody Pedido pedido) {
        return service.create(pedido);
    }

    @PutMapping("/{id}")
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemPedido adicionarItem(@PathVariable Long id, @RequestBody AdicionarItemRequest request) {
        return itemPedidoService.adicionarItem(id, request);
    }

    @PutMapping("/{id}/itens/{idPrato}")
    public ItemPedido alterarItem(@PathVariable Long id, @PathVariable Long idPrato,
                                  @RequestBody AlterarItemRequest request) {
        return itemPedidoService.alterarItem(id, idPrato, request);
    }

    @DeleteMapping("/{id}/itens/{idPrato}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@PathVariable Long id, @PathVariable Long idPrato) {
        itemPedidoService.removerItem(id, idPrato);
    }

    @PostMapping("/{id}/entregar")
    public Pedido entregar(@PathVariable Long id) {
        return service.entregar(id);
    }
}
