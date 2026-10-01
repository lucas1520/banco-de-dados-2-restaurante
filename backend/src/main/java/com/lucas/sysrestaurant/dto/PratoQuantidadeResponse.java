package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.ItemPedido;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record PratoQuantidadeResponse(Long idPrato, String nome, int quantidade) {

    public static List<PratoQuantidadeResponse> agrupar(Collection<ItemPedido> itens) {
        Map<Long, PratoQuantidadeResponse> porPrato = new LinkedHashMap<>();
        for (ItemPedido item : itens) {
            Long idPrato = item.getPrato().getIdPrato();
            porPrato.merge(idPrato,
                    new PratoQuantidadeResponse(idPrato, item.getPrato().getNome(), item.getQuantidade()),
                    (a, b) -> new PratoQuantidadeResponse(idPrato, a.nome(), a.quantidade() + b.quantidade()));
        }
        return porPrato.values().stream()
                .sorted(Comparator.comparing(PratoQuantidadeResponse::nome))
                .toList();
    }
}
