package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.Pedido;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public record PedidoContaResponse(Pedido pedido, List<ItemPedido> itens, BigDecimal valorTotal,
                                  int tempoPrepMinutos) {

    public static PedidoContaResponse de(Pedido pedido, List<ItemPedido> itens) {
        List<ItemPedido> ordenados = itens.stream()
                .sorted(Comparator.comparing(i -> i.getPrato().getIdPrato()))
                .toList();
        BigDecimal valorTotal = ordenados.stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int tempo = ordenados.stream()
                .mapToInt(i -> (i.getPrato().getTempoPrep() == null ? 0 : i.getPrato().getTempoPrep()) * i.getQuantidade())
                .sum();
        return new PedidoContaResponse(pedido, ordenados, valorTotal, tempo);
    }
}
