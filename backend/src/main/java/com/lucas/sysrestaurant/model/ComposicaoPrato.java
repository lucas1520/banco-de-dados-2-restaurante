package com.lucas.sysrestaurant.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "composicao_prato")
@IdClass(ComposicaoPratoId.class)
public class ComposicaoPrato {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_prato", nullable = false)
    private Prato prato;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_ingrediente", nullable = false)
    private Ingrediente ingrediente;

    private BigDecimal quantidade;

    public ComposicaoPrato() {
    }

    public ComposicaoPrato(Prato prato, Ingrediente ingrediente, BigDecimal quantidade) {
        this.prato = prato;
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public Prato getPrato() {
        return prato;
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}
