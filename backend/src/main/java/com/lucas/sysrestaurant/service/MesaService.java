package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.AbrirMesaRequest;
import com.lucas.sysrestaurant.dto.PedidoContaResponse;
import com.lucas.sysrestaurant.dto.PratoQuantidadeResponse;
import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Mesa;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.MesaRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private final MesaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;

    public MesaService(MesaRepository repository, ClienteRepository clienteRepository,
                       ItemPedidoRepository itemPedidoRepository, PedidoRepository pedidoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Mesa> findAll() {
        return repository.findAll();
    }

    public Mesa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrado: " + id));
    }

    public Mesa create(Mesa mesa) {
        mesa.setIdMesa(null);
        mesa.setDisponivel(true);
        return repository.save(mesa);
    }

    public Mesa update(Long id, Mesa mesa) {
        findById(id);
        mesa.setIdMesa(id);
        return repository.save(mesa);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PratoQuantidadeResponse> pratosDaMesa(Long idMesa) {
        findById(idMesa);
        return PratoQuantidadeResponse.agrupar(
                itemPedidoRepository.findByPedido_Cliente_Mesa_IdMesaAndPedido_Cliente_PagoFalse(idMesa));
    }

    @Transactional(readOnly = true)
    public List<PedidoContaResponse> pedidosDaMesa(Long idMesa) {
        findById(idMesa);
        Map<Long, List<ItemPedido>> itensPorPedido = itemPedidoRepository
                .findByPedido_Cliente_Mesa_IdMesaAndPedido_Cliente_PagoFalse(idMesa).stream()
                .collect(Collectors.groupingBy(i -> i.getPedido().getIdPedido()));
        return pedidoRepository.findByCliente_Mesa_IdMesaAndCliente_PagoFalseOrderByIdPedido(idMesa).stream()
                .map(p -> PedidoContaResponse.de(p, itensPorPedido.getOrDefault(p.getIdPedido(), List.of())))
                .toList();
    }

    @Transactional
    public Cliente abrirMesa(Long id, AbrirMesaRequest request) {
        if (request == null || request.nomeCliente() == null || request.nomeCliente().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do cliente é obrigatório");
        }
        Mesa mesa = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrado: " + id));
        Cliente cliente = clienteRepository.save(new Cliente(request.nomeCliente().trim(), mesa, LocalDateTime.now()));
        mesa.setDisponivel(false);
        repository.save(mesa);
        return cliente;
    }
}
