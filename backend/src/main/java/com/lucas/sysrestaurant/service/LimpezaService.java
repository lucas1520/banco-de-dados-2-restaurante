package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.ContagemTabelasResponse;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.ComposicaoPratoRepository;
import com.lucas.sysrestaurant.repository.FuncionarioRepository;
import com.lucas.sysrestaurant.repository.IngredienteRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.MesaRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import com.lucas.sysrestaurant.repository.PratoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LimpezaService {

    private static final String TRUNCATE = "TRUNCATE TABLE item_pedido, pedidos, composicao_prato, cliente, mesa, "
            + "funcionario, prato, ingrediente RESTART IDENTITY CASCADE";

    private final EntityManager em;
    private final MesaRepository mesas;
    private final ClienteRepository clientes;
    private final FuncionarioRepository funcionarios;
    private final PedidoRepository pedidos;
    private final PratoRepository pratos;
    private final IngredienteRepository ingredientes;
    private final ItemPedidoRepository itens;
    private final ComposicaoPratoRepository composicoes;

    public LimpezaService(EntityManager em, MesaRepository mesas, ClienteRepository clientes,
                          FuncionarioRepository funcionarios, PedidoRepository pedidos, PratoRepository pratos,
                          IngredienteRepository ingredientes, ItemPedidoRepository itens,
                          ComposicaoPratoRepository composicoes) {
        this.em = em;
        this.mesas = mesas;
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.pedidos = pedidos;
        this.pratos = pratos;
        this.ingredientes = ingredientes;
        this.itens = itens;
        this.composicoes = composicoes;
    }

    @Transactional
    public ContagemTabelasResponse limpar() {
        ContagemTabelasResponse removidos = new ContagemTabelasResponse(mesas.count(), clientes.count(),
                funcionarios.count(), pratos.count(), ingredientes.count(), composicoes.count(), pedidos.count(),
                itens.count());
        em.createNativeQuery(TRUNCATE).executeUpdate();
        return removidos;
    }
}
