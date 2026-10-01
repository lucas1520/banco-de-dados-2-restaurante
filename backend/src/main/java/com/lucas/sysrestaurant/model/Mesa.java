package com.lucas.sysrestaurant.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesa;

    private Boolean disponivel;

    private Integer cadeiras;

    public Mesa() {
    }

    public Mesa(Boolean disponivel, Integer cadeiras) {
        this.disponivel = disponivel;
        this.cadeiras = cadeiras;
    }

    public Long getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Long idMesa) {
        this.idMesa = idMesa;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Integer getCadeiras() {
        return cadeiras;
    }

    public void setCadeiras(Integer cadeiras) {
        this.cadeiras = cadeiras;
    }
}
