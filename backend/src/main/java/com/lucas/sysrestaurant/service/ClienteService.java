package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.ContaClienteResponse;
import com.lucas.sysrestaurant.dto.PagarClienteResponse;
import com.lucas.sysrestaurant.dto.PedidoContaResponse;
import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Mesa;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.MesaRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final MesaRepository mesaRepository;

    public ClienteService(ClienteRepository repository, PedidoRepository pedidoRepository,
                           ItemPedidoRepository itemPedidoRepository, MesaRepository mesaRepository) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.mesaRepository = mesaRepository;
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + id));
    }

    public Cliente create(Cliente cliente) {
        cliente.setIdCliente(null);
        cliente.setPago(false);
        cliente.setDataChegada(LocalDateTime.now());
        return repository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        Cliente existente = findById(id);
        cliente.setIdCliente(id);
        cliente.setPago(existente.getPago());
        cliente.setDataChegada(existente.getDataChegada());
        return repository.save(cliente);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ContaClienteResponse conta(Long id) {
        Cliente cliente = findById(id);
        List<ItemPedido> itens = itemPedidoRepository.findByPedido_Cliente_IdCliente(id);

        List<PedidoContaResponse> pedidos = pedidoRepository.findByCliente_IdCliente(id).stream()
                .sorted(Comparator.comparing(Pedido::getIdPedido))
                .map(pedido -> {
                    List<ItemPedido> itensDoPedido = itens.stream()
                            .filter(i -> i.getPedido().getIdPedido().equals(pedido.getIdPedido()))
                            .toList();
                    return PedidoContaResponse.de(pedido, itensDoPedido);
                })
                .toList();

        BigDecimal total = pedidos.stream()
                .map(PedidoContaResponse::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ContaClienteResponse(cliente, pedidos, total);
    }

    @Transactional
    public PagarClienteResponse pagar(Long id) {
        Cliente cliente = findById(id);
        Mesa mesa = cliente.getMesa();
        if (mesa != null) {
            mesa = mesaRepository.findByIdForUpdate(mesa.getIdMesa()).orElseThrow();
        }

        if (Boolean.TRUE.equals(cliente.getPago())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já pagou a conta: " + id);
        }
        if (pedidoRepository.existsByCliente_IdClienteAndEntregueFalse(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cliente possui pedidos que ainda não foram entregues: " + id);
        }

        BigDecimal total = subtotal(itemPedidoRepository.findByPedido_Cliente_IdCliente(id));
        cliente.setPago(true);
        repository.save(cliente);

        boolean mesaLiberada = false;
        long pendentes = 0;
        if (mesa != null) {
            pendentes = repository.countByMesa_IdMesaAndPagoFalseAndIdClienteNot(mesa.getIdMesa(), id);
            if (pendentes == 0) {
                mesa.setDisponivel(true);
                mesaRepository.save(mesa);
                mesaLiberada = true;
            }
        }
        return new PagarClienteResponse(cliente, total, mesaLiberada, (int) pendentes);
    }

    private BigDecimal subtotal(List<ItemPedido> itens) {
        return itens.stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
