package com.lucas.sysrestaurant.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
@IdClass(ItemPedidoId.class)
public class ItemPedido {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_prato", nullable = false)
    private Prato prato;

    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Prato prato, Integer quantidade, BigDecimal precoUnitario) {
        this.pedido = pedido;
        this.prato = prato;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Prato getPrato() {
        return prato;
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
