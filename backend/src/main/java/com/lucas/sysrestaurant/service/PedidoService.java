package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.FuncionarioRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ItemPedidoService itemPedidoService;

    public PedidoService(PedidoRepository repository, ItemPedidoRepository itemPedidoRepository,
                          ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository,
                          ItemPedidoService itemPedidoService) {
        this.repository = repository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.itemPedidoService = itemPedidoService;
    }

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    public Pedido findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id));
    }

    public Pedido create(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente é obrigatório");
        }
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getIdCliente())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado: " + pedido.getCliente().getIdCliente()));
        if (Boolean.TRUE.equals(cliente.getPago())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cliente já pagou a conta, não é possível criar pedido: " + cliente.getIdCliente());
        }

        pedido.setIdPedido(null);
        pedido.setEntregue(false);
        pedido.setCliente(cliente);
        pedido.setFuncionario(buscarFuncionario(pedido.getFuncionario()));
        return repository.save(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        Pedido existente = findById(id);
        if (Boolean.TRUE.equals(existente.getEntregue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Pedido já foi entregue, não é possível editar: " + id);
        }
        existente.setFuncionario(buscarFuncionario(pedido.getFuncionario()));
        return repository.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = findById(id);
        if (Boolean.TRUE.equals(pedido.getEntregue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Pedido já foi entregue, não é possível excluir: " + id);
        }
        itemPedidoService.removerItensDoPedido(id);
        repository.deleteById(id);
    }

    public Pedido entregar(Long id) {
        Pedido pedido = findById(id);
        if (Boolean.TRUE.equals(pedido.getEntregue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pedido já foi entregue: " + id);
        }
        if (itemPedidoRepository.findByPedido_IdPedido(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não possui itens: " + id);
        }
        pedido.setEntregue(true);
        return repository.save(pedido);
    }

    private Funcionario buscarFuncionario(Funcionario informado) {
        if (informado == null || informado.getMatricula() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionário é obrigatório");
        }
        Funcionario funcionario = funcionarioRepository.findById(informado.getMatricula())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Funcionário não encontrado: " + informado.getMatricula()));
        if (Boolean.FALSE.equals(funcionario.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Funcionário inativo, não pode ser responsável por pedido: " + funcionario.getNome());
        }
        return funcionario;
    }
}
