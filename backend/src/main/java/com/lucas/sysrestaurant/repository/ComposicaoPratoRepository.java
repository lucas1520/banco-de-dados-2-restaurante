package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.ComposicaoPratoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComposicaoPratoRepository extends JpaRepository<ComposicaoPrato, ComposicaoPratoId> {

    List<ComposicaoPrato> findByPrato_IdPrato(Long idPrato);
}
