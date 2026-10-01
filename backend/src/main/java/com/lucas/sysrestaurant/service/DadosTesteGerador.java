package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.model.Cliente;
import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.model.Ingrediente;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Mesa;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.model.Prato;
import com.lucas.sysrestaurant.service.DadosTesteCatalogo.FuncionarioModelo;
import com.lucas.sysrestaurant.service.DadosTesteCatalogo.IngredienteModelo;
import com.lucas.sysrestaurant.service.DadosTesteCatalogo.ItemReceita;
import com.lucas.sysrestaurant.service.DadosTesteCatalogo.PratoModelo;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

final class DadosTesteGerador {

    record Conjunto(List<Mesa> mesas, List<Funcionario> funcionarios, List<Ingrediente> ingredientes,
                    List<Prato> pratos, List<ComposicaoPrato> composicoes, List<Cliente> clientes,
                    List<Pedido> pedidos, List<ItemPedido> itens) {
    }

    private static final LocalTime ABERTURA = LocalTime.of(11, 0);
    private static final int ALMOCO_INICIO = 11 * 60 + 30;
    private static final int ALMOCO_FIM = 14 * 60 + 30;
    private static final int JANTAR_INICIO = 18 * 60 + 30;
    private static final int JANTAR_FIM = 22 * 60 + 30;
    private static final int MESAS_OCUPADAS_HOJE = 5;

    private final Random rng;
    private final LocalDateTime agora;
    private final LocalDate hoje;
    private final Random cpfRng;
    private final Set<String> cpfsUsados;

    private final List<Mesa> mesas = new ArrayList<>();
    private final List<Funcionario> funcionarios = new ArrayList<>();
    private final List<Ingrediente> ingredientes = new ArrayList<>();
    private final List<Prato> pratos = new ArrayList<>();
    private final List<ComposicaoPrato> composicoes = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<ItemPedido> itens = new ArrayList<>();

    private final Map<Prato, List<ComposicaoPrato>> composicaoDoPrato = new HashMap<>();
    private final Map<Prato, Integer> popularidade = new HashMap<>();
    private final Map<Funcionario, LocalDate> ultimoDiaDosInativos = new HashMap<>();
    private Prato pratoSemComposicao;

    DadosTesteGerador(Random rng, LocalDateTime agora, Set<String> cpfsUsados) {
        this.rng = rng;
        this.agora = agora;
        this.hoje = agora.toLocalDate();
        this.cpfRng = new Random(rng.nextLong());
        this.cpfsUsados = new HashSet<>(cpfsUsados);
    }

    Conjunto gerar() {
        criarMesas();
        criarFuncionarios();
        criarIngredientesEPratos();

        for (LocalDate dia = hoje.withDayOfYear(1); dia.isBefore(hoje); dia = dia.plusDays(1)) {
            gerarDiaPassado(dia);
        }
        gerarHoje();

        atualizarDisponibilidadeDasMesas();
        return new Conjunto(mesas, funcionarios, ingredientes, pratos, composicoes, clientes, pedidos, itens);
    }

    private void criarMesas() {
        for (int cadeiras : DadosTesteCatalogo.CADEIRAS_DAS_MESAS) {
            mesas.add(new Mesa(true, cadeiras));
        }
    }

    private void criarFuncionarios() {
        long dias = ChronoUnit.DAYS.between(hoje.withDayOfYear(1), hoje);
        for (FuncionarioModelo m : DadosTesteCatalogo.FUNCIONARIOS) {
            Funcionario f = new Funcionario(m.nome(), m.cargo(), novoCpf());
            f.setAtivo(m.ativo());
            funcionarios.add(f);
            if (!m.ativo()) {
                long saida = dias / 4 + rng.nextLong(dias / 2 + 1);
                ultimoDiaDosInativos.put(f, hoje.withDayOfYear(1).plusDays(saida));
            }
        }
    }

    private void criarIngredientesEPratos() {
        Map<String, Ingrediente> porNome = new HashMap<>();
        for (IngredienteModelo m : DadosTesteCatalogo.INGREDIENTES) {
            Ingrediente ing = new Ingrediente(m.nome(), new BigDecimal(m.preco()), m.estoque());
            ingredientes.add(ing);
            porNome.put(m.nome(), ing);
        }
        for (PratoModelo m : DadosTesteCatalogo.PRATOS) {
            Prato prato = new Prato(m.nome(), new BigDecimal(m.valor()), m.tempoPrep());
            prato.setAtivo(m.ativo());
            pratos.add(prato);
            popularidade.put(prato, 1 + rng.nextInt(10));

            List<ComposicaoPrato> receita = new ArrayList<>();
            for (ItemReceita r : m.receita()) {
                receita.add(new ComposicaoPrato(prato, porNome.get(r.ingrediente()), new BigDecimal(r.quantidade())));
            }
            composicaoDoPrato.put(prato, receita);
            composicoes.addAll(receita);
            if (receita.isEmpty()) {
                pratoSemComposicao = prato;
            }
        }
    }

    private String novoCpf() {
        while (true) {
            int[] d = new int[11];
            for (int i = 0; i < 9; i++) {
                d[i] = cpfRng.nextInt(10);
            }
            boolean todosIguais = true;
            for (int i = 1; i < 9; i++) {
                todosIguais &= d[i] == d[0];
            }
            if (todosIguais) {
                continue;
            }
            d[9] = digitoVerificador(d, 9);
            d[10] = digitoVerificador(d, 10);
            String cpf = String.format("%d%d%d.%d%d%d.%d%d%d-%d%d",
                    d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10]);
            if (cpfsUsados.add(cpf)) {
                return cpf;
            }
        }
    }

    private static int digitoVerificador(int[] d, int quantos) {
        int soma = 0;
        for (int i = 0; i < quantos; i++) {
            soma += d[i] * (quantos + 1 - i);
        }
        int resto = soma * 10 % 11;
        return resto == 10 ? 0 : resto;
    }

    private void gerarDiaPassado(LocalDate dia) {
        for (LocalDateTime chegada : chegadasDoDia(dia, quantidadeDeClientes(dia))) {
            Mesa mesa = rng.nextInt(100) < 6 ? null : mesaAleatoria();
            Cliente cliente = novoCliente(mesa, chegada, true);
            int quantosPedidos = rng.nextInt(100) < 4 ? 0 : numeroDePedidos();
            for (int i = 0; i < quantosPedidos; i++) {
                Pedido pedido = novoPedido(true, cliente, funcionarioDoDia(dia));
                adicionarItens(pedido, pratos, false);
            }
        }
    }

    private int quantidadeDeClientes(LocalDate dia) {
        if (rng.nextInt(100) < 6) {
            return 0;
        }
        DayOfWeek semana = dia.getDayOfWeek();
        if (semana == DayOfWeek.SATURDAY || semana == DayOfWeek.SUNDAY) {
            return 8 + rng.nextInt(13);
        }
        if (semana == DayOfWeek.FRIDAY) {
            return 3 + rng.nextInt(8);
        }
        return 1 + rng.nextInt(6);
    }

    private List<LocalDateTime> chegadasDoDia(LocalDate dia, int quantos) {
        List<LocalDateTime> chegadas = new ArrayList<>();
        for (int i = 0; i < quantos; i++) {
            boolean almoco = rng.nextInt(100) < 45;
            chegadas.add(horario(dia, almoco ? ALMOCO_INICIO : JANTAR_INICIO, almoco ? ALMOCO_FIM : JANTAR_FIM));
        }
        Collections.sort(chegadas);
        return chegadas;
    }

    private LocalDateTime horario(LocalDate dia, int minutoInicio, int minutoFim) {
        int minuto = minutoInicio + rng.nextInt(Math.max(1, minutoFim - minutoInicio));
        return dia.atTime(minuto / 60, minuto % 60, rng.nextInt(60));
    }

    private void gerarHoje() {
        List<LocalDateTime> chegadas = chegadasDeHoje(13 + rng.nextInt(4));
        int sentados = 8 + rng.nextInt(2);
        int primeiroSentado = chegadas.size() - sentados;

        List<Mesa> embaralhadas = new ArrayList<>(mesas);
        Collections.shuffle(embaralhadas, rng);
        List<Mesa> mesasOcupadas = embaralhadas.subList(0, MESAS_OCUPADAS_HOJE);

        List<Funcionario> ativos = funcionarios.stream().filter(Funcionario::getAtivo).toList();
        List<Prato> pratosAtivos = pratos.stream().filter(Prato::getAtivo).toList();

        for (int i = 0; i < chegadas.size(); i++) {
            boolean sentado = i >= primeiroSentado;
            int s = i - primeiroSentado;
            Mesa mesa;
            if (sentado) {
                mesa = s == 0 ? null : mesasOcupadas.get((s - 1) % MESAS_OCUPADAS_HOJE);
            } else {
                mesa = rng.nextInt(100) < 10 ? null : mesaAleatoria();
            }
            Cliente cliente = novoCliente(mesa, chegadas.get(i), !sentado);

            if (!sentado) {
                int quantosPedidos = rng.nextInt(100) < 4 ? 0 : numeroDePedidos();
                for (int p = 0; p < quantosPedidos; p++) {
                    adicionarItens(novoPedido(true, cliente, sortear(ativos)), pratosAtivos, false);
                }
                continue;
            }

            boolean garantido = s >= 1 && s <= 3;
            int quantosPedidos = garantido ? 1 + rng.nextInt(3) : (rng.nextInt(100) < 10 ? 0 : 1 + rng.nextInt(3));
            for (int p = 0; p < quantosPedidos; p++) {
                boolean aberto = p == quantosPedidos - 1 && (garantido || rng.nextInt(100) < 70);
                Pedido pedido = novoPedido(!aberto, cliente, sortear(ativos));
                adicionarItens(pedido, pratosAtivos, aberto);
            }
        }
    }

    private List<LocalDateTime> chegadasDeHoje(int quantos) {
        LocalDateTime inicio = hoje.atTime(ABERTURA);
        LocalDateTime fim = agora.minusMinutes(5);
        if (fim.isAfter(hoje.atTime(23, 0))) {
            fim = hoje.atTime(23, 0);
        }
        if (fim.isBefore(inicio.plusMinutes(30))) {
            inicio = hoje.atStartOfDay();
            if (fim.isBefore(inicio.plusMinutes(1))) {
                fim = inicio.plusMinutes(1);
            }
        }
        int minutoInicio = inicio.getHour() * 60 + inicio.getMinute();
        int minutoFim = fim.getHour() * 60 + fim.getMinute();
        List<LocalDateTime> chegadas = new ArrayList<>();
        for (int i = 0; i < quantos; i++) {
            chegadas.add(horario(hoje, minutoInicio, minutoFim));
        }
        Collections.sort(chegadas);
        return chegadas;
    }

    private Cliente novoCliente(Mesa mesa, LocalDateTime chegada, boolean pago) {
        String nome = sortear(DadosTesteCatalogo.NOMES) + " " + sortear(DadosTesteCatalogo.SOBRENOMES);
        Cliente cliente = new Cliente(nome, mesa, chegada);
        cliente.setPago(pago);
        clientes.add(cliente);
        return cliente;
    }

    private Pedido novoPedido(boolean entregue, Cliente cliente, Funcionario funcionario) {
        Pedido pedido = new Pedido(entregue, cliente, funcionario);
        pedidos.add(pedido);
        return pedido;
    }

    private Funcionario funcionarioDoDia(LocalDate dia) {
        List<Funcionario> elegiveis = funcionarios.stream()
                .filter(f -> f.getAtivo() || !dia.isAfter(ultimoDiaDosInativos.get(f)))
                .toList();
        return sortear(elegiveis);
    }

    private Mesa mesaAleatoria() {
        return sortear(mesas);
    }

    private int numeroDePedidos() {
        int r = rng.nextInt(100);
        return r < 35 ? 1 : r < 70 ? 2 : r < 90 ? 3 : 4;
    }

    private int numeroDeItens() {
        int r = rng.nextInt(100);
        return r < 25 ? 1 : r < 55 ? 2 : r < 80 ? 3 : r < 92 ? 4 : 5;
    }

    private int quantidadeDoItem() {
        int r = rng.nextInt(100);
        return r < 50 ? 1 : r < 80 ? 2 : r < 94 ? 3 : 4;
    }

    private void adicionarItens(Pedido pedido, List<Prato> candidatos, boolean baixarEstoque) {
        int adicionados = 0;
        for (Prato prato : sortearPratos(candidatos, numeroDeItens())) {
            int quantidade = quantidadeDoItem();
            if (baixarEstoque) {
                while (quantidade > 0 && !temEstoque(prato, quantidade)) {
                    quantidade--;
                }
                if (quantidade == 0) {
                    continue;
                }
                baixarEstoque(prato, quantidade);
            }
            itens.add(new ItemPedido(pedido, prato, quantidade, prato.getValor()));
            adicionados++;
        }
        if (adicionados == 0) {
            itens.add(new ItemPedido(pedido, pratoSemComposicao, 1, pratoSemComposicao.getValor()));
        }
    }

    private boolean temEstoque(Prato prato, int unidades) {
        for (ComposicaoPrato c : composicaoDoPrato.get(prato)) {
            BigDecimal necessario = c.getQuantidade().multiply(BigDecimal.valueOf(unidades));
            if (BigDecimal.valueOf(c.getIngrediente().getEstoque()).compareTo(necessario) < 0) {
                return false;
            }
        }
        return true;
    }

    private void baixarEstoque(Prato prato, int unidades) {
        for (ComposicaoPrato c : composicaoDoPrato.get(prato)) {
            int baixa = c.getQuantidade().multiply(BigDecimal.valueOf(unidades)).intValue();
            Ingrediente ing = c.getIngrediente();
            ing.setEstoque(ing.getEstoque() - baixa);
        }
    }

    private List<Prato> sortearPratos(List<Prato> candidatos, int quantos) {
        List<Prato> restantes = new ArrayList<>(candidatos);
        List<Prato> escolhidos = new ArrayList<>();
        while (escolhidos.size() < quantos && !restantes.isEmpty()) {
            int total = 0;
            for (Prato p : restantes) {
                total += popularidade.get(p);
            }
            int ponto = rng.nextInt(total);
            for (int i = 0; i < restantes.size(); i++) {
                ponto -= popularidade.get(restantes.get(i));
                if (ponto < 0) {
                    escolhidos.add(restantes.remove(i));
                    break;
                }
            }
        }
        return escolhidos;
    }

    private <T> T sortear(List<T> lista) {
        return lista.get(rng.nextInt(lista.size()));
    }

    private void atualizarDisponibilidadeDasMesas() {
        Set<Mesa> ocupadas = new HashSet<>();
        for (Cliente c : clientes) {
            if (!c.getPago() && c.getMesa() != null) {
                ocupadas.add(c.getMesa());
            }
        }
        for (Mesa m : mesas) {
            m.setDisponivel(!ocupadas.contains(m));
        }
    }
}
