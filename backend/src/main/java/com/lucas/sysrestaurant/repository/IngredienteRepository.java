package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
}
