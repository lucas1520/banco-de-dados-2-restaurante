package com.lucas.sysrestaurant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nome;

    private Boolean pago = false;

    private LocalDateTime dataChegada;

    @ManyToOne
    @JoinColumn(name = "id_mesa", nullable = true)
    private Mesa mesa;

    public Cliente() {
    }

    public Cliente(String nome, Mesa mesa, LocalDateTime dataChegada) {
        this.nome = nome;
        this.mesa = mesa;
        this.pago = false;
        this.dataChegada = dataChegada;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public LocalDateTime getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDateTime dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
