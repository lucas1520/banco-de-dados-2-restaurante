package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PratoRepository extends JpaRepository<Prato, Long> {

    List<Prato> findByAtivoTrueOrderByNome();
}
