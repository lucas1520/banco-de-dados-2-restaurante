package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.ContagemTabelasResponse;
import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.FuncionarioRepository;
import com.lucas.sysrestaurant.repository.IngredienteRepository;
import com.lucas.sysrestaurant.repository.MesaRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import com.lucas.sysrestaurant.repository.PratoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DadosTesteService {

    private static final long SEED = 20260101L;

    private final EntityManager em;
    private final MesaRepository mesas;
    private final ClienteRepository clientes;
    private final FuncionarioRepository funcionarios;
    private final PedidoRepository pedidos;
    private final PratoRepository pratos;
    private final IngredienteRepository ingredientes;

    public DadosTesteService(EntityManager em, MesaRepository mesas, ClienteRepository clientes,
                             FuncionarioRepository funcionarios, PedidoRepository pedidos, PratoRepository pratos,
                             IngredienteRepository ingredientes) {
        this.em = em;
        this.mesas = mesas;
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.pedidos = pedidos;
        this.pratos = pratos;
        this.ingredientes = ingredientes;
    }

    @Transactional
    public ContagemTabelasResponse inserir() {
        Set<String> cpfsExistentes = funcionarios.findAll().stream()
                .map(Funcionario::getCpf)
                .collect(Collectors.toSet());
        DadosTesteGerador.Conjunto dados =
                new DadosTesteGerador(new Random(SEED), LocalDateTime.now(), cpfsExistentes).gerar();

        mesas.saveAll(dados.mesas());
        funcionarios.saveAll(dados.funcionarios());
        ingredientes.saveAll(dados.ingredientes());
        pratos.saveAll(dados.pratos());
        dados.composicoes().forEach(em::persist);
        clientes.saveAll(dados.clientes());
        pedidos.saveAll(dados.pedidos());
        dados.itens().forEach(em::persist);

        return new ContagemTabelasResponse(dados.mesas().size(), dados.clientes().size(),
                dados.funcionarios().size(), dados.pratos().size(), dados.ingredientes().size(),
                dados.composicoes().size(), dados.pedidos().size(), dados.itens().size());
    }
}
