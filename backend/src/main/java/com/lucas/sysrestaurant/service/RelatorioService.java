package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.ClientePedidosResponse;
import com.lucas.sysrestaurant.dto.DiaDemandaResponse;
import com.lucas.sysrestaurant.dto.IngredienteUsoResponse;
import com.lucas.sysrestaurant.dto.LucroResponse;
import com.lucas.sysrestaurant.dto.PedidoContaResponse;
import com.lucas.sysrestaurant.dto.PedidosFuncionarioResponse;
import com.lucas.sysrestaurant.dto.PratoQuantidadeResponse;
import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.model.Ingrediente;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.repository.ClienteRepository;
import com.lucas.sysrestaurant.repository.ComposicaoPratoRepository;
import com.lucas.sysrestaurant.repository.FuncionarioRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RelatorioService {

    private static final int TOP = 3;

    private final FuncionarioRepository funcionarioRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ComposicaoPratoRepository composicaoPratoRepository;

    public RelatorioService(FuncionarioRepository funcionarioRepository, PedidoRepository pedidoRepository,
                            ItemPedidoRepository itemPedidoRepository, ClienteRepository clienteRepository,
                            ComposicaoPratoRepository composicaoPratoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.clienteRepository = clienteRepository;
        this.composicaoPratoRepository = composicaoPratoRepository;
    }

    public PedidosFuncionarioResponse pedidosDoFuncionario(Long matricula, LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        Funcionario funcionario = funcionarioRepository.findById(matricula)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Funcionario não encontrado: " + matricula));
        LocalDateTime de = inicio.atStartOfDay();
        LocalDateTime ate = fim.plusDays(1).atStartOfDay();

        Map<Long, List<ItemPedido>> itensPorPedido = itemPedidoRepository.findByFuncionarioNoPeriodo(matricula, de, ate)
                .stream()
                .collect(Collectors.groupingBy(i -> i.getPedido().getIdPedido()));
        Map<Long, List<Pedido>> pedidosPorCliente = pedidoRepository.findByFuncionarioNoPeriodo(matricula, de, ate)
                .stream()
                .collect(Collectors.groupingBy(p -> p.getCliente().getIdCliente(), LinkedHashMap::new, Collectors.toList()));

        List<ClientePedidosResponse> clientes = pedidosPorCliente.values().stream()
                .map(pedidos -> new ClientePedidosResponse(
                        pedidos.get(0).getCliente(),
                        pedidos.stream()
                                .map(p -> PedidoContaResponse.de(p, itensPorPedido.getOrDefault(p.getIdPedido(), List.of())))
                                .toList()))
                .sorted(Comparator.comparing((ClientePedidosResponse c) -> c.cliente().getDataChegada())
                        .thenComparing(c -> c.cliente().getIdCliente()))
                .toList();
        return new PedidosFuncionarioResponse(funcionario, inicio, fim, clientes);
    }

    public LucroResponse lucro(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        LocalDateTime de = inicio.atStartOfDay();
        LocalDateTime ate = fim.plusDays(1).atStartOfDay();

        Map<Long, List<ComposicaoPrato>> composicoes = composicoesPorPrato();
        BigDecimal bruto = BigDecimal.ZERO;
        BigDecimal custo = BigDecimal.ZERO;
        for (ItemPedido item : itemPedidoRepository.findPagosByChegadaNoPeriodo(de, ate)) {
            BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
            bruto = bruto.add(item.getPrecoUnitario().multiply(quantidade));
            for (ComposicaoPrato c : composicoes.getOrDefault(item.getPrato().getIdPrato(), List.of())) {
                custo = custo.add(c.getQuantidade().multiply(c.getIngrediente().getPreco()).multiply(quantidade));
            }
        }
        bruto = bruto.setScale(2, RoundingMode.HALF_UP);
        custo = custo.setScale(2, RoundingMode.HALF_UP);
        int clientes = (int) clienteRepository
                .countByPagoTrueAndDataChegadaGreaterThanEqualAndDataChegadaLessThan(de, ate);
        return new LucroResponse(inicio, fim, clientes, bruto, custo, bruto.subtract(custo));
    }

    public List<DiaDemandaResponse> demanda(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        LocalDateTime de = inicio.atStartOfDay();
        LocalDateTime ate = fim.plusDays(1).atStartOfDay();

        Map<LocalDate, Long> clientesPorDia = clienteRepository
                .findByDataChegadaGreaterThanEqualAndDataChegadaLessThan(de, ate).stream()
                .collect(Collectors.groupingBy(c -> c.getDataChegada().toLocalDate(), Collectors.counting()));
        Map<LocalDate, List<ItemPedido>> itensPorDia = itemPedidoRepository.findByChegadaNoPeriodo(de, ate).stream()
                .collect(Collectors.groupingBy(i -> i.getPedido().getCliente().getDataChegada().toLocalDate()));
        Map<Long, List<ComposicaoPrato>> composicoes = composicoesPorPrato();

        return clientesPorDia.entrySet().stream()
                .map(e -> {
                    List<ItemPedido> itens = itensPorDia.getOrDefault(e.getKey(), List.of());
                    return new DiaDemandaResponse(e.getKey(), e.getValue().intValue(),
                            pratosMaisPedidos(itens), ingredientesMaisUsados(itens, composicoes));
                })
                .sorted(Comparator.comparingInt(DiaDemandaResponse::clientes).reversed()
                        .thenComparing(DiaDemandaResponse::dia))
                .toList();
    }

    private List<PratoQuantidadeResponse> pratosMaisPedidos(List<ItemPedido> itens) {
        return PratoQuantidadeResponse.agrupar(itens).stream()
                .sorted(Comparator.comparingInt(PratoQuantidadeResponse::quantidade).reversed()
                        .thenComparing(PratoQuantidadeResponse::nome))
                .limit(TOP)
                .toList();
    }

    private List<IngredienteUsoResponse> ingredientesMaisUsados(List<ItemPedido> itens,
                                                                 Map<Long, List<ComposicaoPrato>> composicoes) {
        Map<Long, IngredienteUsoResponse> uso = new LinkedHashMap<>();
        for (ItemPedido item : itens) {
            BigDecimal quantidade = BigDecimal.valueOf(item.getQuantidade());
            for (ComposicaoPrato c : composicoes.getOrDefault(item.getPrato().getIdPrato(), List.of())) {
                Ingrediente ingrediente = c.getIngrediente();
                BigDecimal usado = c.getQuantidade().multiply(quantidade);
                uso.merge(ingrediente.getIdIngrediente(),
                        new IngredienteUsoResponse(ingrediente.getIdIngrediente(), ingrediente.getNome(), usado),
                        (a, b) -> new IngredienteUsoResponse(a.idIngrediente(), a.nome(), a.quantidade().add(b.quantidade())));
            }
        }
        return uso.values().stream()
                .sorted(Comparator.comparing(IngredienteUsoResponse::quantidade).reversed()
                        .thenComparing(IngredienteUsoResponse::nome))
                .limit(TOP)
                .toList();
    }

    private Map<Long, List<ComposicaoPrato>> composicoesPorPrato() {
        return composicaoPratoRepository.findAll().stream()
                .collect(Collectors.groupingBy(c -> c.getPrato().getIdPrato()));
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Período inválido: a data final é anterior à data inicial");
        }
    }
}
