package com.lucas.sysrestaurant.model;

import java.io.Serializable;
import java.util.Objects;

public class ComposicaoPratoId implements Serializable {

    private Long prato;

    private Long ingrediente;

    public ComposicaoPratoId() {
    }

    public ComposicaoPratoId(Long prato, Long ingrediente) {
        this.prato = prato;
        this.ingrediente = ingrediente;
    }

    public Long getPrato() {
        return prato;
    }

    public void setPrato(Long prato) {
        this.prato = prato;
    }

    public Long getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Long ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComposicaoPratoId that)) {
            return false;
        }
        return Objects.equals(prato, that.prato) && Objects.equals(ingrediente, that.ingrediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prato, ingrediente);
    }
}
