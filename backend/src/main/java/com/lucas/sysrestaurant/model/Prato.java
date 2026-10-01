package com.lucas.sysrestaurant.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prato")
public class Prato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrato;

    private String nome;

    private BigDecimal valor;

    private Integer tempoPrep;

    private Boolean ativo = true;

    public Prato() {
    }

    public Prato(String nome, BigDecimal valor, Integer tempoPrep) {
        this.nome = nome;
        this.valor = valor;
        this.tempoPrep = tempoPrep;
    }

    public Long getIdPrato() {
        return idPrato;
    }

    public void setIdPrato(Long idPrato) {
        this.idPrato = idPrato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getTempoPrep() {
        return tempoPrep;
    }

    public void setTempoPrep(Integer tempoPrep) {
        this.tempoPrep = tempoPrep;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
