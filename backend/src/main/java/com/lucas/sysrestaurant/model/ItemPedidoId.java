package com.lucas.sysrestaurant.model;

import java.io.Serializable;
import java.util.Objects;

public class ItemPedidoId implements Serializable {

    private Long pedido;

    private Long prato;

    public ItemPedidoId() {
    }

    public ItemPedidoId(Long pedido, Long prato) {
        this.pedido = pedido;
        this.prato = prato;
    }

    public Long getPedido() {
        return pedido;
    }

    public void setPedido(Long pedido) {
        this.pedido = pedido;
    }

    public Long getPrato() {
        return prato;
    }

    public void setPrato(Long prato) {
        this.prato = prato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemPedidoId that)) {
            return false;
        }
        return Objects.equals(pedido, that.pedido) && Objects.equals(prato, that.prato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedido, prato);
    }
}
